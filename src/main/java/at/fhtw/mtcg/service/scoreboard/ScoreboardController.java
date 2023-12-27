package at.fhtw.mtcg.service.scoreboard;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.model.User;

import java.util.List;

public class ScoreboardController {
    public Response getScoreBoard() {
        UnitOfWork unitOfWork = new UnitOfWork();
        try (unitOfWork) {
            UserRepository userRepository = new UserRepository(unitOfWork);
            List<User> userList = userRepository.getAllScores();
            StringBuilder scoreBoard = new StringBuilder("Scoreboard");
            for(User userdata: userList){
                scoreBoard.append(STR."\n\{userdata.getScore()} \{userdata.getUsername()}");
            }
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    scoreBoard.toString()
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
