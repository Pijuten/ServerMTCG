package at.fhtw.mtcg.service.user;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.model.User;

public class UserController extends Controller {

    public Response addUser(User user) {
        UnitOfWork unitOfWork = new UnitOfWork();
        try (unitOfWork) {
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
            e.printStackTrace();

            unitOfWork.rollbackTransaction();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }
}
