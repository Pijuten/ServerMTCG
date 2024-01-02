package at.fhtw.mtcg.service.stats;

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

class StatsServiceTest {
    StatsService statsService;
    StatsController statsController;
    Request request;
    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        this.statsService = new StatsService();
        this.statsController = Mockito.mock(StatsController.class);
        this.request = Mockito.mock(Request.class);

        Field field = StatsService.class.getDeclaredField("statsController");
        field.setAccessible(true);
        field.set(statsService,statsController);
    }

    @Test
    void getParamTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.GET);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(statsController.getStats(request)).thenReturn(expectedResponse);

        assertEquals(statsService.handleRequest(request),expectedResponse);
    }
    @Test
    void postParamTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.POST);

        Response expectedResponse = new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "[]");

        assertEquals(statsService.handleRequest(request).get(),expectedResponse.get());
    }
}