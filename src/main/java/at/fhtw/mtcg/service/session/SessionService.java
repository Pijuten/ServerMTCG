package at.fhtw.mtcg.service.session;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionService implements Service {

    private final SessionController sessionController;
    public SessionService(){this.sessionController=new SessionController();}
    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.GET) {
                //return this.weatherController.getWeatherPerRepository();
            } else if (request.getMethod() == Method.POST) {

                return sessionController.loginUser(request);
            }
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "[]"
            );
        }catch (Exception e){
            e.printStackTrace();
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "[]"
            );
        }
    }
}
