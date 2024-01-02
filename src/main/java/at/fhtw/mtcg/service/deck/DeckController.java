package at.fhtw.mtcg.service.deck;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.PackageRepository;
import at.fhtw.mtcg.helper.TokenVerification;
import at.fhtw.mtcg.model.Card;
import at.fhtw.mtcg.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.Collection;
import java.util.List;

public class DeckController extends Controller {
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
                json=json.concat(STR."{\"Cardid\": \"\{card.getId()}\", \"Cardname\": \"\{card.getName()}\", \"Damage\": \"\{card.getDamage()}\"},");
            }
            json = json.substring(0, json.length() - 1);
            json = json.concat("]");
            unitOfWork.finishWork();
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    json
            );
        }catch (Exception e){
            unitOfWork.rollbackTransaction();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }
    public Response getDeckPlain(Request request){
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
            StringBuilder contentString = new StringBuilder();
            for(Card card:deck){
                contentString.append(STR."\n \{card.getName()} \{card.getDamage()}");
            }
            unitOfWork.finishWork();
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    contentString.toString()
            );
        }catch (Exception e){
            unitOfWork.rollbackTransaction();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }
    public Response setDeck(Request request){
        try {
            Collection <Card> cards = getObjectMapper().readValue(request.getBody(), new TypeReference<List<Card>>(){});
            if(cards.size()!=4)
                throw new RuntimeException("Either to many or to few cards");
        } catch (JsonProcessingException e) {
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
        TokenVerification tokenVerification = new TokenVerification();
        User user = tokenVerification.verifyToken(request);
        if(user==null){
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    "{ \"message\" : \"Not Logged in\" }"
            );
        }
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            Collection <Card> cards = getObjectMapper().readValue(request.getBody(), new TypeReference<List<Card>>(){});
            if(cards.size()!=4)
                throw new RuntimeException("Either to many or to few cards");
            PackageRepository packageRepository = new PackageRepository(unitOfWork);
            packageRepository.unsetDeck(user);
            for(Card card:cards){
                packageRepository.addCardToDeck(card,user);
            }
            unitOfWork.commitTransaction();
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "Success"
            );
        }catch (Exception e){
            unitOfWork.rollbackTransaction();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }
}
