package at.fhtw.mtcg.service.trade;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;

public class TradeService implements Service {
    private TradeController tradeController;
    public TradeService(){
        this.tradeController = new TradeController();
    }
    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.GET) {
                return tradeController.getTrades(request);
            } else if (request.getMethod().equals(Method.POST)) {
                if(request.getPathParts().size() > 1)
                   return tradeController.acceptTrade(request);
                return tradeController.createTrade(request);
            } else if (request.getMethod() == Method.DELETE && request.getPathParts().size() > 1){
                return tradeController.deleteTrade(request);
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
