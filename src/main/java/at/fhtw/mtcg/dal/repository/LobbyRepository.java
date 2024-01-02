package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.DataAccessException;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.model.User;
import at.fhtw.mtcg.model.Lobby;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LobbyRepository {
    UnitOfWork unitOfWork;

    public LobbyRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public int create(User user) {
        try (PreparedStatement preparedStatement = unitOfWork.prepareStatement("""
                    INSERT into lobby(username1) values (?)
                """)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.executeUpdate();
            PreparedStatement psGetId = unitOfWork.prepareStatement("""
                    Select lobbyid from lobby where username1 = ? Order by lobbyid Desc
                    """);
            psGetId.setString(1, user.getUsername());
            ResultSet resultSet = psGetId.executeQuery();
            if (resultSet.next()) return resultSet.getInt("lobbyid");
            return 0;
        } catch (Exception e) {
            throw new DataAccessException("Insert nicht erfolgreich", e);
        }
    }

    public void delete(int lobbyId) {

        try (PreparedStatement preparedStatement = unitOfWork.prepareStatement("""
                DELETE FROM LOBBY WHERE lobbyid=?
                """)) {
            preparedStatement.setInt(1, lobbyId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("Update nicht erfolgreich", e);
        }
    }

    public Lobby getOpponent(User user) {
        try (PreparedStatement preparedStatement = unitOfWork.prepareStatement("""
                 SELECT * FROM lobby WHERE
                (username1 IS NULL OR username2 IS NULL) AND (
                (username1 != ?) OR
                (username2 != ?));
                 """)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString("username1").isEmpty()) {
                    return new Lobby(resultSet.getInt("lobbyid"), user.getUsername(), resultSet.getString("username2"));
                } else
                    return new Lobby(resultSet.getInt("lobbyid"), resultSet.getString("username1"), user.getUsername());
            }
            return null;
        } catch (Exception e) {
            throw new DataAccessException("Update nicht erfolgreich", e);
        }
    }


    public void update(Lobby lobby) {
        try (PreparedStatement preparedStatement = unitOfWork.prepareStatement("""
                UPDATE lobby set lobbyid=?, gamestatus=?, battlelog=?, username1=?, username2=? where lobbyid=?
                """)) {
            preparedStatement.setInt(1, lobby.getLobbyId());
            preparedStatement.setInt(2, lobby.getGameStatus());
            preparedStatement.setString(3, lobby.getBattleLog());
            preparedStatement.setString(4, lobby.getUsername1());
            preparedStatement.setString(5, lobby.getUsername2());
            preparedStatement.setInt(6, lobby.getLobbyId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("Update nicht erfolgreich", e);
        }
    }

    public Lobby getBattle(User user) {
        try (PreparedStatement preparedStatement = unitOfWork.prepareStatement("""
                SELECT * FROM lobby WHERE username1=? or username2=? ORDER BY lobbyid DESC
                """)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return new Lobby(resultSet.getInt("lobbyid"), resultSet.getInt("gamestatus"), resultSet.getString("battlelog"), resultSet.getString("username1"), resultSet.getString("username2"));
            return null;
        } catch (Exception e) {
            throw new DataAccessException("Update nicht erfolgreich", e);
        }
    }
    public List<Lobby> getLobbyByUsername(User user) {
        try (PreparedStatement preparedStatement = unitOfWork.prepareStatement("""
                SELECT * FROM lobby WHERE username1=? or username2=? ORDER BY lobbyid DESC
                """)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Lobby> lobbyList = new ArrayList<>();
            while (resultSet.next())
                lobbyList.add(new Lobby(resultSet.getInt("lobbyid"), resultSet.getInt("gamestatus"), resultSet.getString("battlelog"), resultSet.getString("username1"), resultSet.getString("username2")));
            return lobbyList;
        } catch (Exception e) {
            throw new DataAccessException("SELECT nicht erfolgreich", e);
        }
    }

    public boolean checkLobbyEmpty(int lobbyId) {
        try (PreparedStatement preparedStatement = unitOfWork.prepareStatement("""
                SELECT * FROM lobby WHERE lobbyid=? and username1 is not null and username2 is not null
                """)) {
            preparedStatement.setInt(1, lobbyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.next();
        } catch (Exception e) {
            throw new DataAccessException("SELECT nicht erfolgreich", e);
        }
    }
}
