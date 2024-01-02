package at.fhtw.mtcg.service.history;

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

class HistoryServiceTest {
    HistoryService historyService;
    HistoryController historyController;
    Request request;

    @BeforeEach
    void setup() throws IllegalAccessException, NoSuchFieldException {
        this.historyService = new HistoryService();
        this.historyController = Mockito.mock(HistoryController.class);
        this.request = Mockito.mock(Request.class);

        Field field = HistoryService.class.getDeclaredField("historyController");
        field.setAccessible(true);
        field.set(historyService,historyController);
    }

    @Test
    void getPathTest(){
        Mockito.when(request.getMethod()).thenReturn(Method.GET);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(historyController.getGameHistory(request)).thenReturn(expectedResponse);

        assertEquals(historyService.handleRequest(request),expectedResponse);
    }


    @Test
    void postPathTest(){
        Mockito.when(request.getMethod()).thenReturn(Method.POST);

        Response expectedResponse = new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "[]");

        assertEquals(historyService.handleRequest(request).get(),expectedResponse.get());
    }
}