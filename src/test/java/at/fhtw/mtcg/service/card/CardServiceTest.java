package at.fhtw.mtcg.service.card;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CardServiceTest {
    CardService cardService;
    CardController cardController;
    Request request;

    @BeforeEach
    void setup() throws IllegalAccessException, NoSuchFieldException {
        this.cardService = new CardService();
        this.cardController = Mockito.mock(CardController.class);
        this.request = Mockito.mock(Request.class);

        Field field = CardService.class.getDeclaredField("cardController");
        field.setAccessible(true);
        field.set(cardService,cardController);
    }

    @Test
    void getPath(){
        Mockito.when(request.getMethod()).thenReturn(Method.GET);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(cardController.getCardsUser(request)).thenReturn(expectedResponse);

        assertEquals(cardService.handleRequest(request),expectedResponse);
    }
    @Test
    void postPath(){
        Mockito.when(request.getMethod()).thenReturn(Method.POST);

        Response expectedResponse = new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "[]");

        assertEquals(cardService.handleRequest(request).get(),expectedResponse.get());
    }
}