package at.fhtw.mtcg.service.transaction;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.helper.TokenVerification;
import at.fhtw.mtcg.model.Card;
import at.fhtw.mtcg.model.User;

import java.util.Collection;

public class TransactionController extends Controller {
    public Response purchasePackage(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            TokenVerification tokenVerification = new TokenVerification();
            User user = tokenVerification.verifyToken(request);
            if(user==null){
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        "{ \"message\" : \"Not Loggedin\" }"
                );
            }
            if(user.getCurrency()<40){
                throw  new RuntimeException("No Money");
            }


            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Purchase successful\" }"
            );
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
