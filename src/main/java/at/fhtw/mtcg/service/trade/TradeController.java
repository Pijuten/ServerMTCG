package at.fhtw.mtcg.service.trade;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.PackageRepository;
import at.fhtw.mtcg.dal.repository.TradeRepository;
import at.fhtw.mtcg.helper.TokenVerification;
import at.fhtw.mtcg.model.Card;
import at.fhtw.mtcg.model.Trade;
import at.fhtw.mtcg.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.util.Collection;
import java.util.Objects;

public class TradeController extends Controller {
    private static final Response ErrorResponse =

            new Response(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
    );
    public Response getTrades(Request request) {
        TokenVerification tokenVerification = new TokenVerification();
        User user = tokenVerification.verifyToken(request);
        if(user==null){
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    "{ \"message\" : \"Not Logged in\" }"
            );
        }

        UnitOfWork unitOfWork = new UnitOfWork();
        try (unitOfWork) {
            TradeRepository tradeRepository = new TradeRepository(unitOfWork);
            Collection<Trade> tradeCollection =  tradeRepository.getAllTrades();
            StringBuilder builder = new StringBuilder("[");
            for(Trade trade:tradeCollection){
                if(Objects.equals(user.getUsername(), trade.getUsername()))
                    continue;
                builder.append(STR."{\"TradeId\":\"\{trade.getTradeId()}\",\"CardId\":\"\{trade.getCardId()}\",\"Type\":\"\{trade.getType()}\",\"MinDamage\":\"\{trade.getType()}\"}");
            }
            builder.append("]");
            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    builder.toString()
            );
        }catch (Exception e){
            unitOfWork.finishWork();
            return ErrorResponse;
        }
    }

    public Response acceptTrade(Request request) {
        String tradeId = request.getPathParts().get(1);

        //Get card to trade without quotes
        String requestBodyCardId = request.getBody().replace("\"","");


        TokenVerification tokenVerification = new TokenVerification();
        User user = tokenVerification.verifyToken(request);
        if(user==null){
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    "{ \"message\" : \"Not Logged in\" }"
            );
        }
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            TradeRepository tradeRepository = new TradeRepository(unitOfWork);
            Trade trade = tradeRepository.getTradeById(tradeId);
            if(trade.getUsername().equals(user.getUsername()))
                throw new RuntimeException("Same Username");

            PackageRepository packageRepository = new PackageRepository(unitOfWork);
            System.out.println(requestBodyCardId);
            Card cardRequest = packageRepository.getCardById(requestBodyCardId);
            Card cardTrade = packageRepository.getCardById(trade.getCardId());
            //When Card to trade is not valid or card to trade doesn't belong to the user throw error
            if(cardRequest==null || !Objects.equals(cardRequest.getUsername(), user.getUsername()))
                throw new RuntimeException("Request card not available");
            //When Card from trade offer is not valid or card doesn't belong to the trader
            if(cardTrade==null || !Objects.equals(cardTrade.getUsername(), trade.getUsername()))
                throw new RuntimeException("Request card not available");
            if(cardRequest.getDamage()<trade.getMinDamage())
                throw new RuntimeException("To low Damage card");

            String usernameRequest = user.getUsername();

            String usernameTrade = trade.getUsername();

            cardRequest.setUsername(usernameTrade);
            packageRepository.updateCard(cardRequest);

            cardTrade.setUsername(usernameRequest);
            packageRepository.updateCard(cardTrade);

            tradeRepository.deleteTrade(tradeId);

            unitOfWork.commitTransaction();
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "SUCCESS"
            );
        } catch (Exception e) {
            unitOfWork.rollbackTransaction();
            return ErrorResponse;
        }
    }

    public Response createTrade(Request request) {
        TokenVerification tokenVerification = new TokenVerification();
        User user = tokenVerification.verifyToken(request);
        if(user==null){
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    "{ \"message\" : \"Not Logged in\" }"
            );
        }
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            Trade trade = getObjectMapper().readValue(request.getBody(),Trade.class);
            trade.setUsername(user.getUsername());
            TradeRepository tradeRepository = new TradeRepository(unitOfWork);
            tradeRepository.createTrade(trade);
            unitOfWork.commitTransaction();
            return new Response(
                    HttpStatus.OK,
                    ContentType.PLAIN_TEXT,
                    "SUCCESS"
            );
        } catch (JsonProcessingException e) {
            return ErrorResponse;
        } catch (Exception e) {
            unitOfWork.rollbackTransaction();
            return ErrorResponse;
        }
    }

    public Response deleteTrade(Request request) {
        TokenVerification tokenVerification = new TokenVerification();
        User user = tokenVerification.verifyToken(request);
        if(user==null){
            return new Response(
                    HttpStatus.FORBIDDEN,
                    ContentType.JSON,
                    "{ \"message\" : \"Not Logged in\" }"
            );
        }
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            String tradeId = request.getPathParts().get(1);
            TradeRepository tradeRepository = new TradeRepository(unitOfWork);
            Trade trade = tradeRepository.getTradeById(tradeId);
            if(!trade.getUsername().equals(user.getUsername()))
                throw new RuntimeException("Trade doesnt belong to user");

            tradeRepository.deleteTrade(tradeId);
            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK,ContentType.PLAIN_TEXT,"Success");
        } catch (Exception e) {
            unitOfWork.rollbackTransaction();
            return ErrorResponse;
        }
    }
}
