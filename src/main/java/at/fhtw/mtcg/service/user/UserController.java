package at.fhtw.mtcg.service.user;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserController extends Controller {

    public Response addUser(Request request) {

        UnitOfWork unitOfWork = new UnitOfWork();
        try (unitOfWork) {
            User user = this.getObjectMapper().readValue(request.getBody(), User.class);
            UserRepository userRepository = new UserRepository(unitOfWork);
            PasswordHash passwordHash = new PasswordHash();
            passwordHash.getHashedPassword(user);
            userRepository.createUser(user);
            unitOfWork.commitTransaction();
            unitOfWork.finishWork();
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "Successful"
            );
        } catch (Exception e) {

            unitOfWork.rollbackTransaction();
            unitOfWork.finishWork();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }
}
