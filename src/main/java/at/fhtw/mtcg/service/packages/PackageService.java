package at.fhtw.mtcg.service.packages;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;

public class PackageService implements Service {

    private PackageController packageController;
    public PackageService(){this.packageController =new PackageController();}
    @Override
    public Response handleRequest(Request request) {
        try {
            if (request.getMethod() == Method.GET) {
                //return this.weatherController.getWeatherPerRepository();
            } else if (request.getMethod() == Method.POST && request.getHeaderMap().getHeader("Authorization").replace("Bearer ","").equals("admin-mtcgToken")) {

                return packageController.createPackage(request);
            }
            System.out.println(request.getHeaderMap().getHeader("Authorization"));
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "[]"
            );
        }catch (Exception e){
            e.printStackTrace();
            return new Response(
                    HttpStatus.BAD_REQUEST,
                    ContentType.JSON,
                    "[]"
            );
        }
    }
}
