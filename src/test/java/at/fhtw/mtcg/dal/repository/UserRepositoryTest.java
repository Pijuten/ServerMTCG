package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @BeforeEach
    void EmptyDb() throws Exception {
        UnitOfWork unitOfWork = new UnitOfWork();
        try (unitOfWork) {
            unitOfWork.prepareStatement("""
                TRUNCATE table userdata,cards CASCADE
                """);
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
        assertEquals(userRepository.getUserByUsername("stani").iterator().next().getUsername(),"stani");
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