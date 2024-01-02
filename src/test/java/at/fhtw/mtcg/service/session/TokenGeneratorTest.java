package at.fhtw.mtcg.service.session;

import at.fhtw.mtcg.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenGeneratorTest {
    @Test
    void nullUserTest(){
        User user = null;
        TokenGenerator tokenGenerator = new TokenGenerator(user);
        assertNull(tokenGenerator.getToken());
    }
    @Test
    void emptyUserTest(){
        User user = new User("","");
        TokenGenerator tokenGenerator = new TokenGenerator(user);
        assertNull(tokenGenerator.getToken());
    }
    @Test
    void UserTest(){
        User user = new User("stani","");
        TokenGenerator tokenGenerator = new TokenGenerator(user);
        assertEquals(tokenGenerator.getToken(),"stani-mtcgToken");
    }
}