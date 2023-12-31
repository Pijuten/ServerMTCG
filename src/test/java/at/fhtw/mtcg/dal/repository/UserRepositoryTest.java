package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

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
    void createUser() {
        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);
        User user = new User("stani","password");
        userRepository.createUser(user);
        assertEquals(userRepository.getUserByUsername("stani").getUsername(),"stani");
        unitOfWork.commitTransaction();
        unitOfWork.finishWork();
    }
    @Test
    void createEmptyUser() {
        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);
        User user = new User("","");
        assertThrows(RuntimeException.class,() ->userRepository.createUser(user));
        unitOfWork.commitTransaction();
        unitOfWork.finishWork();
    }

    @Test
    void getUserByUsername() {
    }

    @Test
    void updateUser() {
    }
}