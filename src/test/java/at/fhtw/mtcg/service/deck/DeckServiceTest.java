package at.fhtw.mtcg.service.deck;

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

class DeckServiceTest {
    DeckController deckControllerMock;
    DeckService deckService;
    Request requestMock;
    Response expectedResponse = new Response(HttpStatus.OK, ContentType.JSON, "{ \"message\" : \"Purchase successful\" }");

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        this.deckControllerMock = Mockito.mock(DeckController.class);

        this.deckService=new DeckService();
        Field field = DeckService.class.getDeclaredField("deckController");
        field.setAccessible(true);
        field.set(deckService,deckControllerMock);

        requestMock = Mockito.mock(Request.class);
    }
    @Test
    void postPathTest(){
        Mockito.when(requestMock.getMethod()).thenReturn(Method.PUT);
        Mockito.when(deckControllerMock.setDeck(requestMock)).thenReturn(expectedResponse);
        assertEquals(deckService.handleRequest(requestMock),expectedResponse);
    }
    @Test
    void getPathTest(){
        Mockito.when(requestMock.getMethod()).thenReturn(Method.GET);
        Mockito.when(deckControllerMock.getDeck(requestMock)).thenReturn(expectedResponse);
        assertEquals(deckService.handleRequest(requestMock),expectedResponse);
    }
}