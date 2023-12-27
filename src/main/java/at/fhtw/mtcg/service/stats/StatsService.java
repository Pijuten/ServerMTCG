package at.fhtw.mtcg.service.stats;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;

public class StatsService implements Service {
    StatsController statsController;
    public StatsService(){
        this.statsController = new StatsController();
    }
    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.POST) {

                return statsController.getStats(request);
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
