package at.fhtw.mtcg.service.packages;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
class PackageServiceTest {
    PackageController packageController;
    PackageService packageService;
    Request request;
    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {

        this.packageController = Mockito.mock(PackageController.class);
        this.packageService = new PackageService();
        this.request = Mockito.mock(Request.class);

        Field field = PackageService.class.getDeclaredField("packageController");
        field.setAccessible(true);
        field.set(packageService,packageController);
    }
    @Test
    public void postParameterTest(){

        Mockito.when(request.getMethod()).thenReturn(Method.POST);
        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization: Bearer admin-mtcgToken");

        Response expectedResponse = new Response(HttpStatus.OK, ContentType.PLAIN_TEXT,"Success");
        Mockito.when(request.getHeaderMap()).thenReturn(headerMap);
        Mockito.when(packageController.createPackage(request)).thenReturn(expectedResponse);
        assertEquals(packageService.handleRequest(request),expectedResponse);
    }
}