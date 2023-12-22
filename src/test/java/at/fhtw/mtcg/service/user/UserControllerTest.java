package at.fhtw.mtcg.service.user;

import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerTest {

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
    void addUserTest(){
        Request request = new Request();
        request.setBody("{\"Username\":\"kienboec\", \"Password\":\"daniel\"}");
        UserController userController = new UserController();
        Response response = userController.addUser(request);
        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);
        User user = userRepository.getUserByUsername("kienboec");
        unitOfWork.finishWork();
        assertNotNull(user);
        assertEquals("200",response.get().substring(9,12));
    }
    @Test
    void addEmptyUserTest(){
        Request request = new Request();
        request.setBody("{\"Username\":\"\", \"Password\":\"daniel\"}");
        UserController userController = new UserController();
        Response response = userController.addUser(request);
        assertEquals("500",response.get().substring(9,12));
    }
}