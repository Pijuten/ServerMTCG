package at.fhtw.mtcg.service.deck;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;

public class DeckService implements Service {
    private final DeckController deckController;
    public DeckService(){
        this.deckController=new DeckController();
    }
    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.GET) {
                return deckController.getDeck(request);
            } else if (request.getMethod() == Method.PUT) {
                return deckController.setDeck(request);
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
