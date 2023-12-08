package at.fhtw.mtcg.helper;

import at.fhtw.httpserver.server.HeaderMap;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.model.User;
import at.fhtw.mtcg.service.session.SessionController;
import at.fhtw.mtcg.service.user.UserController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class TokenVerificationTest {

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
    void getUserByTokenTest(){
        Request request = new Request();
        request.setBody("{\"Username\":\"kienboec\", \"Password\":\"daniel\"}");
        UserController userController = new UserController();
        userController.addUser(request);
        SessionController sessionController = new SessionController();
        sessionController.loginUser(request);

        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:kienboec-mtcgToken");
        request.setHeaderMap(headerMap);

        TokenVerification tokenVerification = new TokenVerification();
        User user = tokenVerification.verifyToken(request);
        assertNotNull(user);
    }

    @Test
    void getUserByWrongTokenTest(){
        Request request = new Request();
        request.setBody("{\"Username\":\"kienboec\", \"Password\":\"daniel\"}");
        UserController userController = new UserController();
        userController.addUser(request);
        SessionController sessionController = new SessionController();
        sessionController.loginUser(request);

        HeaderMap headerMap = new HeaderMap();
        headerMap.ingest("Authorization:daw-mtcgToken");
        request.setHeaderMap(headerMap);

        TokenVerification tokenVerification = new TokenVerification();
        assertThrows(RuntimeException.class, ()->tokenVerification.verifyToken(request));
    }

}