package at.fhtw.mtcg.service.transaction;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.event.MouseMotionAdapter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {
    TransactionController transactionController;
    TransactionService transactionService;
    Request request;
    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        this.transactionController = Mockito.mock(TransactionController.class);
        this.transactionService = new TransactionService();
        this.request = Mockito.mock(Request.class);

        Field field = TransactionService.class.getDeclaredField("transactionController");
        field.setAccessible(true);
        field.set(this.transactionService,this.transactionController);
    }
    @Test
    void postPathTest(){
        Mockito.when(request.getMethod()).thenReturn(Method.POST);
        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(transactionController.purchasePackage(request)).thenReturn(expectedResponse);
        assertEquals(transactionService.handleRequest(request),expectedResponse);
    }
    @Test
    void getPathTest(){
        Mockito.when(request.getMethod()).thenReturn(Method.GET);
        Response expectedResponse = new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "[]");
        assertEquals(transactionService.handleRequest(request).get(),expectedResponse.get());
    }
}