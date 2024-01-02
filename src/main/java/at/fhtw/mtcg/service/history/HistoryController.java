    package at.fhtw.mtcg.service.history;

    import at.fhtw.httpserver.http.ContentType;
    import at.fhtw.httpserver.http.HttpStatus;
    import at.fhtw.httpserver.server.Request;
    import at.fhtw.httpserver.server.Response;
    import at.fhtw.mtcg.dal.UnitOfWork;
    import at.fhtw.mtcg.dal.repository.LobbyRepository;
    import at.fhtw.mtcg.helper.TokenVerification;
    import at.fhtw.mtcg.model.Lobby;
    import at.fhtw.mtcg.model.User;

    import java.util.List;

    public class HistoryController {
        public Response getGameHistory(Request request){
            TokenVerification tokenVerification = new TokenVerification();
            User user = tokenVerification.verifyToken(request);
            if(user==null){
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        "{ \"message\" : \"Not Logged in\" }"
                );
            }
            UnitOfWork unitOfWork = new UnitOfWork();
            try (unitOfWork) {
                LobbyRepository lobbyRepository = new LobbyRepository(unitOfWork);
                List<Lobby> lobbyList = lobbyRepository.getLobbyByUsername(user);
                StringBuilder json = new StringBuilder();
                for(Lobby lobby:lobbyList)
                    json.append(STR."{\"User1\":\"\{lobby.getUsername1()}\",\"User2\":\"\{lobby.getUsername2()}\",\"Log\":\"\{lobby.getBattleLog()}\"},");
                if (!json.isEmpty())
                    json = new StringBuilder(json.substring(0, json.length() - 1));
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        json.toString()
                );
            }catch (Exception e){
                unitOfWork.rollbackTransaction();
                return new Response(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ContentType.JSON,
                        "[]"
                );
            }
        }
    }
