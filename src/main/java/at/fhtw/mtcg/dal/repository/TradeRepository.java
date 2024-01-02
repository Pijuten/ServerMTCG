package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.DataAccessException;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.model.Trade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class TradeRepository {

    private final UnitOfWork unitOfWork;

    public TradeRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public Collection<Trade> getAllTrades() {
        try (PreparedStatement preparedStatement = (unitOfWork.prepareStatement("""
                Select * from trades
                """))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            Collection<Trade> tradeCollection = new ArrayList<>();
            while (resultSet.next()) {
                tradeCollection.add(
                        new Trade(resultSet.getString("tradeid"),
                                resultSet.getString("cardid"),
                                resultSet.getString("type"),
                                resultSet.getDouble("mindamge"),
                                resultSet.getString("username")
                        ));
            }
            return tradeCollection;
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich", e);
        }
    }

    public void createTrade(Trade trade) {
        try (PreparedStatement preparedStatement = (unitOfWork.prepareStatement("""
                INSERT INTO trades(tradeid,cardid,type,mindamge,username) VALUES(?,?,?,?,?)
                """))) {
            preparedStatement.setString(1, trade.getTradeId());
            preparedStatement.setString(2, trade.getCardId());
            preparedStatement.setString(3, trade.getType());
            preparedStatement.setDouble(4, trade.getMinDamage());
            preparedStatement.setString(5, trade.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich", e);
        }
    }

    public Trade getTradeById(String id) {

        try (PreparedStatement preparedStatement = (unitOfWork.prepareStatement("""
                Select * FROM trades WHERE tradeid=?
                """))) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return new Trade(
                        resultSet.getString("tradeid"),
                        resultSet.getString("cardid"),
                        resultSet.getString("type"),
                        resultSet.getDouble("mindamge"),
                        resultSet.getString("username")
                );
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich", e);
        }
    }


    public void deleteTrade(String tradeId) {
        try(PreparedStatement preparedStatement =
                    this.unitOfWork.prepareStatement("""
                                Delete from trades where tradeid=?
                             """)) {
            preparedStatement.setString(1,tradeId);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            throw new DataAccessException("Update nicht erfolgreich", e);
        }
    }
}
