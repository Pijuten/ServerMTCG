package at.fhtw.mtcg.service.packages;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.PackageRepository;
import at.fhtw.mtcg.model.Card;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Collection;
import java.util.List;

public class PackageController extends Controller {
    public Response createPackage(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            PackageRepository packageRepository = new PackageRepository(unitOfWork);
            Collection <Card> cards = getObjectMapper().readValue(request.getBody(), new TypeReference<List<Card>>(){});
            int maxPackageNumber = packageRepository.getMaxPackageNumber()+1;
            for(Card card:cards){
                card.setDamageType(getDamageType());
                card.setPackageid(maxPackageNumber);
                packageRepository.createCard(card);
            }
            unitOfWork.commitTransaction();
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    "{ \"message\" : \"Creation Success\" }"
            );
        }catch (Exception e){
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }

    }

    private int getDamageType() {
        return 0;
    }
}
