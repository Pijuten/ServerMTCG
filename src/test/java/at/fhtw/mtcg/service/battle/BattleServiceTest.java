package at.fhtw.mtcg.service.battle;

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

class BattleServiceTest {
    BattleService battleService;
    BattleController battleController;
    Request request;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        this.battleService = new BattleService();
        this.battleController = Mockito.mock(BattleController.class);
        this.request = Mockito.mock(Request.class);

        Field field = BattleService.class.getDeclaredField("battleController");
        field.setAccessible(true);
        field.set(battleService,battleController);
    }
    @Test
    void postPathTest(){
        Mockito.when(request.getMethod()).thenReturn(Method.POST);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(battleController.BattleUser(request)).thenReturn(expectedResponse);

        assertEquals(battleService.handleRequest(request),expectedResponse);
    }

    @Test
    void getPath(){
        Mockito.when(request.getMethod()).thenReturn(Method.GET);

        Response expectedResponse = new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "[]");

        assertEquals(battleService.handleRequest(request).get(),expectedResponse.get());
    }
}