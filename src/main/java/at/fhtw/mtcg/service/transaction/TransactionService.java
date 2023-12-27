package at.fhtw.mtcg.service.transaction;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;

public class TransactionService implements Service {

    private final TransactionController transactionController;
    public TransactionService(){this.transactionController =new TransactionController();}
    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.POST) {
                    return transactionController.purchasePackage(request);
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
