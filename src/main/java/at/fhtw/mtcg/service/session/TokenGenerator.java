package at.fhtw.mtcg.service.session;

import at.fhtw.mtcg.model.User;
import lombok.Getter;

public class TokenGenerator {
    public TokenGenerator(User user){
        this.user=user;
        this.token = GenerateToken();
    }
    private String GenerateToken(){
        String username = user.getUsername();
        if(username==null || username.isEmpty())
            return null;
        return STR."\{username}-mtcgToken";
    }
    private final User user;
    @Getter
    private String token;

}