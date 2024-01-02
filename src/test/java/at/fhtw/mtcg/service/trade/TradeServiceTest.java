package at.fhtw.mtcg.service.trade;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TradeServiceTest {
    TradeService tradeService;
    TradeController tradeController;
    Request request;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        this.tradeService = new TradeService();
        this.tradeController = Mockito.mock(TradeController.class);
        this.request = Mockito.mock(Request.class);

        Field field = TradeService.class.getDeclaredField("tradeController");
        field.setAccessible(true);
        field.set(tradeService, tradeController);
    }
    @Test
    void getPathTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.GET);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(tradeController.getTrades(request)).thenReturn(expectedResponse);

        assertEquals(tradeService.handleRequest(request),expectedResponse);
    }
    @Test
    void postPathTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.POST);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(tradeController.createTrade(request)).thenReturn(expectedResponse);

        assertEquals(tradeService.handleRequest(request),expectedResponse);
    }
    @Test
    void postPathWithPartTest(){
        List<String> stringList = new ArrayList<>();
        stringList.addFirst("tradings");
        stringList.add(1,"231d-adwda-12edda");
        Mockito.when(request.getPathParts()).thenReturn(stringList);

        Mockito.when(request.getMethod()).thenReturn(Method.POST);
        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(tradeController.acceptTrade(request)).thenReturn(expectedResponse);

        assertEquals(tradeService.handleRequest(request),expectedResponse);
    }

    @Test
    void putPathTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.PUT);

        Response expectedResponse = new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "[]");

        assertEquals(tradeService.handleRequest(request).get(),expectedResponse.get());
    }
}