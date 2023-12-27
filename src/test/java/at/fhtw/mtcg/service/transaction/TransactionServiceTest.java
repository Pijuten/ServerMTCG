package at.fhtw.mtcg.service.transaction;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {
    @Test
    public void RouteCorrectTest() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        TransactionController transactionControllerMock = Mockito.mock(TransactionController.class);
        TransactionService transactionService = new TransactionService();

        Field field = TransactionService.class.getDeclaredField("transactionController");
        field.setAccessible(true);
        field.set(transactionService, transactionControllerMock);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.JSON, "{ \"message\" : \"Purchase successful\" }");
        Request request = Mockito.mock(Request.class);
        Mockito.when(request.getMethod()).thenReturn(Method.GET);
        Mockito.when(request.getPathname()).thenReturn("transactions");

        List<String> pathList = new ArrayList<>();
        pathList.add("transactions");
        pathList.add("packages");
        Mockito.when(request.getPathParts()).thenReturn(pathList);

        Mockito.when(transactionControllerMock.purchasePackage(request)).thenReturn(expectedResponse);

        // Act
        assertEquals(transactionService.handleRequest(request), expectedResponse);
        //
    }
    @Test
    public void RouteFalsePathpartnameTest() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        TransactionController transactionControllerMock = Mockito.mock(TransactionController.class);
        TransactionService transactionService = new TransactionService();

        Field field = TransactionService.class.getDeclaredField("transactionController");
        field.setAccessible(true);
        field.set(transactionService, transactionControllerMock);

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.JSON, "{ \"message\" : \"Purchase successful\" }");
        Request request = Mockito.mock(Request.class);
        Mockito.when(request.getMethod()).thenReturn(Method.POST);
        Mockito.when(request.getPathname()).thenReturn("transactions");

        List<String> pathList = new ArrayList<>();
        pathList.add("transactions");
        pathList.add("falseParameter");
        Mockito.when(request.getPathParts()).thenReturn(pathList);

        Mockito.when(transactionControllerMock.purchasePackage(request)).thenReturn(expectedResponse);

        // Act
        assertNotEquals(transactionService.handleRequest(request),expectedResponse);
        //
    }
}