package at.fhtw.mtcg.model;

import lombok.Data;

@Data
public class Lobby {
    int lobbyId;
    int gameStatus;
    String battleLog;
    String username1;
    String username2;
}
