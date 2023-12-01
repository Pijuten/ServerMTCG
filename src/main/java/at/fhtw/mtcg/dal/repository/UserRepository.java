package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.model.User;
import at.fhtw.mtcg.dal.DataAccessException;
import at.fhtw.mtcg.dal.UnitOfWork;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserRepository {
    private UnitOfWork unitOfWork;

    public UserRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    public void createUser(User user) {
        if(user.getUsername().isEmpty() || user.getPassword().isEmpty())
            throw new RuntimeException("Username or password is Empty");
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                                 INSERT INTO userdata(username,password,salt)
                                 VALUES (?,?,?)
                             """)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setBytes(2, user.getHashedPassword());
            preparedStatement.setBytes(3, user.getSalt());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich", e);
        }
    }

    public User getUserByUsername(String username) {
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                                 select * from userdata
                                 where username = ?
                             """)) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            Collection<User> userRows = new ArrayList<>();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString(1),
                        resultSet.getBytes(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        resultSet.getInt(6),
                        resultSet.getInt(7),
                        resultSet.getInt(8),
                        resultSet.getString(9),
                        resultSet.getString(10),
                        resultSet.getString(11),
                        resultSet.getBytes(12));
            }
            throw new RuntimeException("No user");
        } catch (SQLException e) {
            throw new DataAccessException("Select nicht erfolgreich", e);
        }
    }
    public void updateUser(User user){
        try (PreparedStatement preparedStatement =
                     this.unitOfWork.prepareStatement("""
                                UPDATE userdata
                                SET
                                  password = ?,\s
                                  token = ?,\s
                                  currency = ?,\s
                                  score = ?,\s
                                  wins = ?,\s
                                  draws = ?,\s
                                  losses = ?,\s
                                  displayName = ?,\s
                                  bio = ?,\s
                                  profileimage = ?,\s
                                  salt = ?
                                WHERE
                                  username = ?;
                             """)) {
            preparedStatement.setBytes(1,user.getHashedPassword());
            preparedStatement.setString(2,user.getToken());
            preparedStatement.setInt(3,user.getScore());
            preparedStatement.setInt(4,user.getCurrency());
            preparedStatement.setInt(5,user.getWin());
            preparedStatement.setInt(6,user.getDraw());
            preparedStatement.setInt(7,user.getLoss());
            preparedStatement.setString(8,user.getDisplayName());
            preparedStatement.setString(9,user.getBio());
            preparedStatement.setString(10,user.getProfileImage());
            preparedStatement.setBytes(11,user.getSalt());
            preparedStatement.setString(12,user.getUsername());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Insert nicht erfolgreich", e);
        }
    }
}