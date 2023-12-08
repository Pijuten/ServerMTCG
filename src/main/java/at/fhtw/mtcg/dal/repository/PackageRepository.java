package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.DataAccessException;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.model.Card;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class PackageRepository {

    private final UnitOfWork unitOfWork;
    public PackageRepository(UnitOfWork unitOfWork){
        this.unitOfWork= unitOfWork;
    }

    public void createCard(Card card){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                     Insert into Cards(cardid, cardname, damage, deck, username, packageid)
                     VALUES (?,?,?,?,?,?)
                """)) {
            preparedStatement.setString(1,card.getId());
            preparedStatement.setString(2,card.getName());
            preparedStatement.setDouble(3,card.getDamage());
            preparedStatement.setBoolean(4,card.getDeck());
            preparedStatement.setString(5,card.getUsername());
            preparedStatement.setInt(6,card.getPackageid());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Card getCardById(String id){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                     SELECT * FROM CARDS
                     WHERE cardid=?
                """)) {
            preparedStatement.setString(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new Card(
                        resultSet.getString("cardid"),
                        resultSet.getString("cardname"),
                        resultSet.getDouble("damage"),
                        resultSet.getBoolean("deck"),
                        resultSet.getString("username"),
                        resultSet.getInt("packageid")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public  Collection<Card> getAllCards(){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                     SELECT * FROM CARDS
                """)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            Collection<Card> cardRows = new ArrayList<>();
            while (resultSet.next()){
                cardRows.add(
                 new Card(
                        resultSet.getString("cardid"),
                        resultSet.getString("cardname"),
                        resultSet.getDouble("damage"),
                        resultSet.getBoolean("deck"),
                        resultSet.getString("username"),
                        resultSet.getInt("packageid")
                ));
            }
            return cardRows;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int getMaxPackageNumber(){
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                                Select max(packageid) from cards
                             """)) {
            ResultSet rs = preparedStatement.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich", e);
        }
    }
}
