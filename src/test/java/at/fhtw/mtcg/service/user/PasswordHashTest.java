package at.fhtw.mtcg.service.user;

import at.fhtw.mtcg.model.User;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHashTest {

    @Test
    void PasswordHashingTest() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeySpecException {
        User user = new User("admin","admin");
        PasswordHash passwordHash = new PasswordHash();
        passwordHash.getHashedPassword(user);
        assertNotNull(user.getHashedPassword());
        assertNotNull(user.getSalt());
    }
    @Test
    void CompareHashCorrectPasswordTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        User user = new User("admin","admin");
        user.setHashedPassword(new byte[]{-25, -33, -113, 13, -50, 105, -69, 55, 23, -121, -36, 106, 43, -80, 119, -75});
        user.setSalt(new byte[]{-94, -67, 105, 35, 21, -54, -53, 87, 83, -47, -56, 43, 79, 98, 41, -1});
        PasswordHash passwordHash = new PasswordHash();
        boolean isSame = passwordHash.compareHash(user);
        assertTrue(isSame);
    }
    @Test
    void CompareHashIncorrectPasswordTest() throws NoSuchAlgorithmException, InvalidKeySpecException {
        User user = new User("admin","wrong");
        user.setHashedPassword(new byte[]{-25, -33, -113, 13, -50, 105, -69, 55, 23, -121, -36, 106, 43, -80, 119, -75});
        user.setSalt(new byte[]{-94, -67, 105, 35, 21, -54, -53, 87, 83, -47, -56, 43, 79, 98, 41, -1});
        PasswordHash passwordHash = new PasswordHash();
        boolean isSame = passwordHash.compareHash(user);
        assertFalse(isSame);
    }
}