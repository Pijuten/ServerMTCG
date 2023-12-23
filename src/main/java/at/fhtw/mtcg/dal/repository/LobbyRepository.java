package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.model.User;
import at.fhtw.mtcg.model.Lobby;

public class LobbyRepository {
    UnitOfWork unitOfWork;
    public LobbyRepository(UnitOfWork unitOfWork){
        this.unitOfWork = unitOfWork;
    }
    public int create(User user) {
        return 0;
    }
    public void delete(int lobbyId) {
    }

    public Lobby getOpponent(User user) {
        return null;
    }


    public void update(Lobby battleLog) {
    }

    public Lobby getBattle(User user) {
        return null;
    }
}
