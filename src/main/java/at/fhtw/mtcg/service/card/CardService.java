package at.fhtw.mtcg.service.card;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;

public class CardService implements Service {
    private final CardController cardController;

    public CardService() {
        cardController  = new CardController();
    }

    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.GET) {
                return cardController.getCardsUser(request);
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
