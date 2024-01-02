package at.fhtw.mtcg.service.session;

import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.model.User;
import at.fhtw.mtcg.service.user.UserController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class SessionControllerTest {
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
    public void loginUserTest(){
        Request request = new Request();
        request.setBody("{\"Username\":\"kienboec\", \"Password\":\"daniel\"}");
        UserController userController = new UserController();
        userController.addUser(request);
        SessionController sessionController = new SessionController();
        Response response = sessionController.loginUser(request);

        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);
        User retrivedUser = userRepository.getUserByUsername("kienboec");
        unitOfWork.finishWork();
        assertEquals("kienboec-mtcgToken", retrivedUser.getToken());
        assertEquals("200",response.get().substring(9,12));
    }
    @Test
    public void loginEmptyUserTest(){
        Request request = new Request();
        request.setBody("{\"Username\":\"kienboec\", \"Password\":\"daniel\"}");
        UserController userController = new UserController();
        userController.addUser(request);
        request.setBody("{\"Username\":\"\", \"Password\":\"daniel\"}");
        SessionController sessionController = new SessionController();
        Response response = sessionController.loginUser(request);

        assertEquals("500",response.get().substring(9,12));
    }
}