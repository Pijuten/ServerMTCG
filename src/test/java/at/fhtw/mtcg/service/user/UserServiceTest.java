package at.fhtw.mtcg.service.user;

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

class UserServiceTest {
    UserService userService;
    UserController userController;
    Request request;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        this.userService = new UserService();
        this.userController = Mockito.mock(UserController.class);
        this.request = Mockito.mock(Request.class);

        Field field = UserService.class.getDeclaredField("userController");
        field.setAccessible(true);
        field.set(userService,userController);
    }
    @Test
    void getPathTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.GET);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(userController.getUserData(request)).thenReturn(expectedResponse);

        assertEquals(userService.handleRequest(request),expectedResponse);
    }
    @Test
    void postPathTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.POST);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(userController.addUser(request)).thenReturn(expectedResponse);

        assertEquals(userService.handleRequest(request),expectedResponse);
    }
    @Test
    void putPathTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.PUT);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(userController.editUserData(request)).thenReturn(expectedResponse);

        assertEquals(userService.handleRequest(request),expectedResponse);
    }@Test
    void deletePathTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.DELETE);

        Response expectedResponse = new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "[]");

        assertEquals(userService.handleRequest(request).get(),expectedResponse.get());
    }

}