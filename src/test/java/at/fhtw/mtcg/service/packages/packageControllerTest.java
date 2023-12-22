package at.fhtw.mtcg.service.packages;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.model.Card;
import org.junit.jupiter.api.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class PackageControllerTest {
    @BeforeEach
    void emptyDb() throws Exception {
        UnitOfWork unitOfWork = new UnitOfWork();
        try (unitOfWork) {
            PreparedStatement preparedStatement = unitOfWork.prepareStatement("""
                    TRUNCATE table userdata,cards CASCADE
                    """);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
            unitOfWork.finishWork();
        }
    }

    @AfterEach
    void emptyDbAfter() throws Exception {
        UnitOfWork unitOfWork = new UnitOfWork();
        try (unitOfWork) {
            PreparedStatement preparedStatement = unitOfWork.prepareStatement("""
                    TRUNCATE table userdata,cards CASCADE
                    """);
            preparedStatement.executeUpdate();
            unitOfWork.commitTransaction();
            unitOfWork.finishWork();
        }
    }

    @Test
    public void addPackage() throws SQLException {
        Request request = new Request();
        request.setBody("[{\"Id\":\"70962948-2bf7-44a9-9ded-8c68eeac7793\", \"Name\":\"WaterGoblin\", \"Damage\":  9.0}, {\"Id\":\"74635fae-8ad3-4295-9139-320ab89c2844\", \"Name\":\"FireSpell\", \"Damage\": 55.0}, {\"Id\":\"ce6bcaee-47e1-4011-a49e-5a4d7d4245f3\", \"Name\":\"Knight\", \"Damage\": 21.0}, {\"Id\":\"a6fde738-c65a-4b10-b400-6fef0fdb28ba\", \"Name\":\"FireSpell\", \"Damage\": 55.0}, {\"Id\":\"a1618f1e-4f4c-4e09-9647-87e16f1edd2d\", \"Name\":\"FireElf\", \"Damage\": 23.0}]");
        PackageController cardController = new PackageController();
        cardController.createPackage(request);
        UnitOfWork unitOfWork = new UnitOfWork();
        PreparedStatement preparedStatement = unitOfWork.prepareStatement("""
                Select * from cards where cardid = ? or cardid=? or cardid=? or cardid=? or cardid=?
                """);
        Card card1 = new Card("70962948-2bf7-44a9-9ded-8c68eeac7793", "WaterGoblin", 9.0);
        Card card2 = new Card("74635fae-8ad3-4295-9139-320ab89c2844", "FireSpell", 55);
        Card card3 = new Card("ce6bcaee-47e1-4011-a49e-5a4d7d4245f3", "Knight", 21);
        Card card4 = new Card("a6fde738-c65a-4b10-b400-6fef0fdb28ba", "FireSpell", 55);
        Card card5 = new Card("a1618f1e-4f4c-4e09-9647-87e16f1edd2d", "FireElf", 23);
        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        cardList.add(card5);
        preparedStatement.setString(1, cardList.get(0).getId());
        preparedStatement.setString(2, cardList.get(1).getId());
        preparedStatement.setString(3, cardList.get(2).getId());
        preparedStatement.setString(4, cardList.get(3).getId());
        preparedStatement.setString(5, cardList.get(4).getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        unitOfWork.finishWork();
        int i = 0;
        while (resultSet.next()) {
            assertEquals(resultSet.getString("cardid"), cardList.get(i).getId());
            assertEquals(resultSet.getString("cardname"), cardList.get(i).getName());
            assertEquals(resultSet.getDouble("damage"), cardList.get(i).getDamage());
            i++;
        }
    }
    @Test
    public void addPackageTwice() throws SQLException {
        Request request = new Request();
        request.setBody("[{\"Id\":\"70962948-2bf7-44a9-9ded-8c68eeac7793\", \"Name\":\"WaterGoblin\", \"Damage\":  9.0}, {\"Id\":\"74635fae-8ad3-4295-9139-320ab89c2844\", \"Name\":\"FireSpell\", \"Damage\": 55.0}, {\"Id\":\"ce6bcaee-47e1-4011-a49e-5a4d7d4245f3\", \"Name\":\"Knight\", \"Damage\": 21.0}, {\"Id\":\"a6fde738-c65a-4b10-b400-6fef0fdb28ba\", \"Name\":\"FireSpell\", \"Damage\": 55.0}, {\"Id\":\"a1618f1e-4f4c-4e09-9647-87e16f1edd2d\", \"Name\":\"FireElf\", \"Damage\": 23.0}]");
        UnitOfWork unitOfWork = new UnitOfWork();

        PreparedStatement preparedStatement = unitOfWork.prepareStatement("""
                Select * from cards where cardid = ? or cardid=? or cardid=? or cardid=? or cardid=?
                """);
        Card card1 = new Card("70962948-2bf7-44a9-9ded-8c68eeac7793", "WaterGoblin", 9.0);
        Card card2 = new Card("74635fae-8ad3-4295-9139-320ab89c2844", "FireSpell", 55);
        Card card3 = new Card("ce6bcaee-47e1-4011-a49e-5a4d7d4245f3", "Knight", 21);
        Card card4 = new Card("a6fde738-c65a-4b10-b400-6fef0fdb28ba", "FireSpell", 55);
        Card card5 = new Card("a1618f1e-4f4c-4e09-9647-87e16f1edd2d", "FireElf", 23);
        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);
        cardList.add(card3);
        cardList.add(card4);
        cardList.add(card5);
        preparedStatement.setString(1, cardList.get(0).getId());
        preparedStatement.setString(2, cardList.get(1).getId());
        preparedStatement.setString(3, cardList.get(2).getId());
        preparedStatement.setString(4, cardList.get(3).getId());
        preparedStatement.setString(5, cardList.get(4).getId());
        ResultSet resultSet = preparedStatement.executeQuery();
        unitOfWork.finishWork();
        int i = 0;

        while (resultSet.next()) {
            assertEquals(resultSet.getString("cardid"),cardList.get(i).getId());
            assertEquals(resultSet.getString("cardname"),cardList.get(i).getName());
            assertEquals(resultSet.getDouble("damage"),cardList.get(i).getDamage());
            i++;
        }
    }
}