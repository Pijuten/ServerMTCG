package at.fhtw.mtcg.service.history;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;

public class HistoryService implements Service {
    private HistoryController historyController;
    public HistoryService(){
        historyController = new HistoryController();
    }
    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod().equals(Method.GET)) {

                return historyController.getGameHistory(request);
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
