package at.fhtw.mtcg.service.scoreboard;

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

class ScoreboardServiceTest {
    ScoreboardService scoreboardService;
    ScoreboardController scoreboardController;
    Request request;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        this.scoreboardService = new ScoreboardService();
        this.scoreboardController = Mockito.mock(ScoreboardController.class);
        this.request = Mockito.mock(Request.class);

        Field field = ScoreboardService.class.getDeclaredField("scoreboardController");
        field.setAccessible(true);
        field.set(scoreboardService,scoreboardController);
    }

    @Test
    void getPathTest(){
        Mockito.when(request.getMethod()).thenReturn(Method.GET);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(scoreboardController.getScoreBoard()).thenReturn(expectedResponse);

        assertEquals(scoreboardService.handleRequest(request),expectedResponse);
    }
    @Test
    void postPathTest(){
        Mockito.when(request.getMethod()).thenReturn(Method.POST);

        Response expectedResponse = new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "[]");

        assertEquals(scoreboardService.handleRequest(request).get(),expectedResponse.get());
    }
}