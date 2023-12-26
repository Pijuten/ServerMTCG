package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.DataAccessException;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.model.Card;
import at.fhtw.mtcg.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class PackageRepository {

    private final UnitOfWork unitOfWork;
    public PackageRepository(UnitOfWork unitOfWork){
        this.unitOfWork= unitOfWork;
    }

    private Collection<Card> getCardFromRs(ResultSet resultSet) throws SQLException {
        Collection<Card> cardRows = new ArrayList<>();
        while (resultSet.next()){
            cardRows.add(
                    new Card(
                            resultSet.getString("cardid"),
                            resultSet.getString("cardname"),
                            resultSet.getDouble("damage"),
                            resultSet.getBoolean("deck"),
                            resultSet.getString("username"),
                            resultSet.getInt("packageid"),
                            resultSet.getInt("damageType"),
                            resultSet.getInt("type")
                    ));
        }
        return cardRows;
    }
    public void createCard(Card card){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                     Insert into Cards(cardid, cardname, damage, deck, username, packageid,damagetype,type)
                     VALUES (?,?,?,?,?,?,?,?)
                """)) {
            preparedStatement.setString(1,card.getId());
            preparedStatement.setString(2,card.getName());
            preparedStatement.setDouble(3,card.getDamage());
            preparedStatement.setBoolean(4,card.getDeck());
            preparedStatement.setString(5,card.getUsername());
            preparedStatement.setInt(6,card.getPackageid());
            preparedStatement.setInt(7,card.getDamageType());
            preparedStatement.setInt(8,card.getType());
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
            Collection<Card> CardsRs = getCardFromRs(preparedStatement.executeQuery());
            if(CardsRs.size()==1){
                Iterator<Card> iterator = CardsRs.iterator();

                // Get the first element
                return iterator.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public  Collection<Card> getCardsByUsername(User user){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                     SELECT * FROM CARDS
                     WHERE username=?
                """)) {
            preparedStatement.setString(1,user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            return getCardFromRs(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public  Collection<Card> getDeck(User user){
        try(PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("""
                     SELECT * FROM CARDS
                     WHERE username=? and deck=true
                """)) {
            preparedStatement.setString(1,user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            return getCardFromRs(resultSet);
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
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich", e);
        }
    }
    public void addNameToMinPackages(User user){
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                                UPDATE cards set username=? where packageid=(SELECT min(packageid) from cards where username is null)
                             """)) {
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich", e);
        }
    }
    public void unsetDeck(User user){
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                                UPDATE cards set deck=false where username=?
                             """)) {
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Update nicht erfolgreich", e);
        }
    }
    public void addCardToDeck(Card card,User user)  {
        try(PreparedStatement preparedStatement =
                    this.unitOfWork.prepareStatement("""
                                UPDATE cards set deck=true  where cardid=? and where username=?
                             """)) {
            preparedStatement.setString(1,card.getId());
            preparedStatement.setString(2,user.getUsername());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            throw new DataAccessException("Update nicht erfolgreich", e);
        }
    }
    public void setInitialDeck(User user){
        try(PreparedStatement preparedStatement =
                    this.unitOfWork.prepareStatement("""
                                UPDATE cards SET deck = true WHERE cardid IN(SELECT cardid from cards where username=? Order BY damage desc LIMIT 4)
                             """)) {
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.executeUpdate();
        }catch (Exception e){
            throw new DataAccessException("Update nicht erfolgreich", e);
        }
    }
}
