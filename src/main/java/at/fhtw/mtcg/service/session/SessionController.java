package at.fhtw.mtcg.service.session;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.model.User;
import at.fhtw.mtcg.service.user.PasswordHash;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionController extends Controller {
    public Response loginUser(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            User user = this.getObjectMapper().readValue(request.getBody(), User.class);
            UserRepository userRepository = new UserRepository(unitOfWork);
            User retrivedUser = userRepository.getUserByUsername(user.getUsername());
            PasswordHash passwordHash = new PasswordHash();
            retrivedUser.setPassword(user.getPassword());
            if(passwordHash.compareHash(retrivedUser)){
                TokenGenerator tokenGenerator = new TokenGenerator(retrivedUser);
                retrivedUser.setToken(tokenGenerator.getToken());
                userRepository.updateUser(retrivedUser);
                unitOfWork.commitTransaction();
                unitOfWork.finishWork();
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{\"Token\":\""+retrivedUser.getToken()+"\"}"
                );
            }else{
                throw new RuntimeException("Password not the same");
            }
        }catch (Exception e){
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
