package at.fhtw.mtcg.service.user;

import at.fhtw.mtcg.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PasswordHash {
    public void getHashedPassword(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(user.getPassword().toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        user.setSalt(salt);
        user.setHashedPassword(factory.generateSecret(spec).getEncoded());
    }
    public boolean compareHash(User user) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Retrieve the stored salt associated with the user
        KeySpec verifySpec = new PBEKeySpec(user.getPassword().toCharArray(), user.getSalt(), 65536, 128);
        SecretKeyFactory verifyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] inputHash = verifyFactory.generateSecret(verifySpec).getEncoded();

        // Compare the generated hash with the stored hash
        return java.util.Arrays.equals(user.getHashedPassword(), inputHash);
    }
}