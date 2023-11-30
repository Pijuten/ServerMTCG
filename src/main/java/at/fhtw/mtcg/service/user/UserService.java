package at.fhtw.mtcg.service.user;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.mtcg.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserService implements Service {
    private final UserController userController;

    public UserService() {
        this.userController = new UserController();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
        System.out.println(request.getBody());
        String json =request.getBody();
        if (request.getMethod() == Method.GET) {
            //return this.weatherController.getWeatherPerRepository();
        } else if (request.getMethod() == Method.POST) {

            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(json, User.class);
                return userController.addUser(user);
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