package at.fhtw.mtcg.service.deck;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.PackageRepository;
import at.fhtw.mtcg.helper.TokenVerification;
import at.fhtw.mtcg.model.Card;
import at.fhtw.mtcg.model.User;

import java.util.Collection;

public class DeckController {
    public Response getDeck(Request request){
        TokenVerification tokenVerification = new TokenVerification();
        User user = tokenVerification.verifyToken(request);
        if(user==null){
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    "{ \"message\" : \"Not Loggedin\" }"
            );
        }
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            PackageRepository packageRepository = new PackageRepository(unitOfWork);
            Collection<Card> deck = packageRepository.getDeck(user);
            String json="[";
            for(Card card:deck){
                json=json.concat("{\"Cardid\": \""+card.getId()+"\", \"Cardname\": \""+card.getName()+"\", \"Damage\": \""+card.getDamage()+"\"},");
            }
            json = json.substring(0, json.length() - 1);
            json = json.concat("]");
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    json
            );
        }catch (Exception e){

            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }
    public void setDeck(Request request){

    }
}
