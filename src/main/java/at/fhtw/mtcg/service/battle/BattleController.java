package at.fhtw.mtcg.service.battle;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.LobbyRepository;
import at.fhtw.mtcg.dal.repository.PackageRepository;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.helper.TokenVerification;
import at.fhtw.mtcg.model.Card;
import at.fhtw.mtcg.model.User;
import at.fhtw.mtcg.model.Lobby;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BattleController {
    public Response BattleUser(Request request){
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
            LobbyRepository lobbyRepository = new LobbyRepository(unitOfWork);
            int lobbyId = lobbyRepository.create(user);
            Lobby isOpponent= lobbyRepository.getOpponent(user);
            if(isOpponent==null){
                Lobby lobby = new Lobby();
                lobbyRepository.update(lobby);
                String json = battle(lobby,unitOfWork);
                lobbyRepository.delete(lobbyId);
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        json
                );
            }else{
                while (isOpponent==null){
                    TimeUnit.SECONDS.sleep(1);
                    isOpponent=lobbyRepository.getOpponent(user);
                }

                Lobby battleLog = lobbyRepository.getBattle(user);
                lobbyRepository.delete(lobbyId);
                return new Response(
                        HttpStatus.OK,
                        ContentType.PLAIN_TEXT,
                        ""
                );
            }

        }catch (Exception e){

            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "Success"
            );
        }
    }

    private String battle(Lobby lobby, UnitOfWork unitOfWork) {
        PackageRepository repository = new PackageRepository(unitOfWork);
        UserRepository userRepository = new UserRepository(unitOfWork);
        Collection<Card> deck1 = repository.getDeck(userRepository.getUserByUsername(lobby.getUsername1()));
        Collection<Card> deck2 = repository.getDeck(userRepository.getUserByUsername(lobby.getUsername2()));
        return "";
    }
}
