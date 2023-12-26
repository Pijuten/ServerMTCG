package at.fhtw.mtcg.service.user;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.helper.TokenVerification;
import at.fhtw.mtcg.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

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

    public Response getUserData(Request request) {
        TokenVerification tokenVerification = new TokenVerification();
        User user = tokenVerification.verifyToken(request);
        if(user==null || !Objects.equals(request.getPathParts().get(1), user.getUsername())){
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    "{ \"message\" : \"Not Logged in\" }"
            );
        }
        return new Response(
                HttpStatus.OK,
                ContentType.JSON,
                STR."{\"username\":\"\{user.getDisplayName()}\",\"Bio\":\"\{user.getBio()}\",\"ProfileImage\":\"\{user.getProfileImage()}\"}"
        );
    }

    public Response editUserData(Request request) {
        TokenVerification tokenVerification = new TokenVerification();
        User user = tokenVerification.verifyToken(request);
        if(user==null || !Objects.equals(request.getPathParts().get(1), user.getUsername())){
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    "{ \"message\" : \"Not Logged in\" }"
            );
        }
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
           User userData =  getObjectMapper().readValue(request.getBody(),User.class);
           user.setDisplayName(userData.getDisplayName());
           user.setBio(userData.getBio());
           user.setProfileImage(userData.getProfileImage());
           UserRepository userRepository = new UserRepository(unitOfWork);
           userRepository.updateUser(user);
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
