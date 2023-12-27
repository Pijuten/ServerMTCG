package at.fhtw.mtcg.service.stats;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.helper.TokenVerification;
import at.fhtw.mtcg.model.User;

public class StatsController extends Controller {
    public Response getStats(Request request){
        TokenVerification tokenVerification = new TokenVerification();
        User user = tokenVerification.verifyToken(request);
        if(user==null){
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    "{ \"message\" : \"Not Loggedin\" }"
            );
        }
        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                STR."{\"Wins\":\"\{user.getWin()}\",\"Losses\":\"\{user.getLoss()}\",\"Draws\":\"\{user.getDraw()}\",}"
        );
    }
}
