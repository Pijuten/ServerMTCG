package at.fhtw.mtcg.service.user;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;

public class UserService implements Service {
    private final UserController userController;

    public UserService() {
        this.userController = new UserController();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
        if (request.getMethod() == Method.GET) {
            return userController.getUserData(request);
        } else if (request.getMethod() == Method.POST) {
                return userController.addUser(request);
        } else if (request.getMethod() == Method.PUT) {
            return userController.editUserData(request);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
        }catch (Exception e){
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "[]"
            );
        }
    }

}
