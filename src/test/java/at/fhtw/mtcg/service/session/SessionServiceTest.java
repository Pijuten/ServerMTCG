package at.fhtw.mtcg.service.session;

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

class SessionServiceTest {
    SessionService sessionService;
    SessionController sessionController;
    Request request;

    @BeforeEach
    void setup() throws IllegalAccessException, NoSuchFieldException {
        this.sessionService = new SessionService();
        this.sessionController = Mockito.mock(SessionController.class);
        this.request = Mockito.mock(Request.class);

        Field field = SessionService.class.getDeclaredField("sessionController");
        field.setAccessible(true);
        field.set(sessionService,sessionController);
    }

    @Test
    void postPathTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.POST);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(sessionController.loginUser(request)).thenReturn(expectedResponse);

        assertEquals(sessionService.handleRequest(request),expectedResponse);
    }
    @Test
    void getPathTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.DELETE);

        Response expectedResponse = new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "[]");

        assertEquals(sessionService.handleRequest(request).get(),expectedResponse.get());
    }
}