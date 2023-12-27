package at.fhtw.mtcg.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lobby {
    int lobbyId;
    //0-draw, 1-user1 won, 2-user2Won
    int gameStatus;
    String battleLog;
    String username1;
    String username2;

    public Lobby(int lobbyid, String username1, String username2) {
        this.lobbyId = lobbyid;
        this.username1 = username1;
        this.username2 = username2;
    }
}
