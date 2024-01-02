    package at.fhtw.mtcg.service.battle;

    import at.fhtw.httpserver.http.ContentType;
    import at.fhtw.httpserver.http.HttpStatus;
    import at.fhtw.httpserver.server.Request;
    import at.fhtw.httpserver.server.Response;
    import at.fhtw.mtcg.dal.UnitOfWork;
    import at.fhtw.mtcg.dal.repository.LobbyRepository;
    import at.fhtw.mtcg.dal.repository.PackageRepository;
    import at.fhtw.mtcg.dal.repository.UserRepository;
    import at.fhtw.mtcg.helper.TokenVerification;
    import at.fhtw.mtcg.model.Card;
    import at.fhtw.mtcg.model.User;
    import at.fhtw.mtcg.model.Lobby;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;
    import java.util.concurrent.TimeUnit;

    public class BattleController {
        public Response BattleUser(Request request){
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
            try(unitOfWork) {
                LobbyRepository lobbyRepository = new LobbyRepository(unitOfWork);
                Lobby isOpponent = lobbyRepository.getOpponent(user);
                if(isOpponent!=null){
                    battle(isOpponent, unitOfWork);
                    lobbyRepository.update(isOpponent);
                    unitOfWork.commitTransaction();
                    return new Response(
                            HttpStatus.OK,
                            ContentType.JSON,
                            isOpponent.getBattleLog()
                    );
                }else{
                    int lobbyId = lobbyRepository.create(user);
                    unitOfWork.commitTransaction();
                    boolean hasNoOpponent = true;
                    while (hasNoOpponent){
                        TimeUnit.SECONDS.sleep(1);
                        hasNoOpponent=lobbyRepository.checkLobbyEmpty(lobbyId);
                    }

                    Lobby battleLog = lobbyRepository.getBattle(user);
                    unitOfWork.commitTransaction();
                    return new Response(
                            HttpStatus.OK,
                            ContentType.PLAIN_TEXT,
                            battleLog.getBattleLog()
                    );
                }

            }catch (Exception e){
                e.printStackTrace();
                unitOfWork.rollbackTransaction();
                return new Response(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ContentType.JSON,
                        "[]"
                );
            }
        }

        private void battle(Lobby lobby, UnitOfWork unitOfWork) {
            StringBuilder BattleLog = new StringBuilder(STR."\{lobby.getUsername1()} vs. \{lobby.getUsername2()}");
            PackageRepository repository = new PackageRepository(unitOfWork);
            UserRepository userRepository = new UserRepository(unitOfWork);
            User user1 = userRepository.getUserByUsername(lobby.getUsername1());
            User user2 = userRepository.getUserByUsername(lobby.getUsername2());
            List<Card> deck1 = new ArrayList<>(repository.getDeck(user1));
            List<Card> deck2 =  new ArrayList<>(repository.getDeck(user2));
            for(int i=0;i<100;i++){
                if(deck1.isEmpty()) {
                    BattleLog.append(STR."\n\{lobby.getUsername2()} won");
                    lobby.setGameStatus(2);
                    lobby.setBattleLog(BattleLog.toString());
                    user2.setWin(user2.getWin()+1);
                    user2.setScore(user2.getScore()-5);
                    user1.setLoss(user1.getLoss()+1);
                    user1.setScore(user1.getScore()+3);
                    userRepository.updateUser(user1);
                    userRepository.updateUser(user2);
                    return;
                }
                if(deck2.isEmpty()) {
                    BattleLog.append(STR."\n\{lobby.getUsername1()} won");
                    lobby.setGameStatus(1);
                    user1.setWin(user1.getWin()+1);
                    user1.setScore(user1.getScore()+3);
                    user2.setLoss(user2.getLoss()+1);
                    user2.setScore(user2.getScore()-5);
                    lobby.setBattleLog(BattleLog.toString());
                    userRepository.updateUser(user1);
                    userRepository.updateUser(user2);
                    return;
                }
                Random random = new Random();
                int randomNumber1 = random.nextInt(deck1.size());
                int randomNumber2 = random.nextInt(deck2.size());
                Card currentCard1 = deck1.get(randomNumber1);
                Card currentCard2 = deck2.get(randomNumber2);
                double multiplier1 = getMultiplier(currentCard1,currentCard2);
                double multiplier2 = getMultiplier(currentCard2,currentCard1);
                double damage1 = currentCard1.getDamage()*multiplier1;
                double damage2 = currentCard2.getDamage()*multiplier2;
                double resultDamage = damage1 + damage2;
                if (resultDamage>0) {
                    deck1.add(currentCard2);
                    deck2.remove(currentCard2);
                } else if (resultDamage<0) {
                    deck2.add(currentCard1);
                    deck1.remove(currentCard1);
                }
                BattleLog.append(STR."\nRound \{i+1}: \{lobby.getUsername1()}: \{currentCard1.getName()} (\{damage1} Damage) vs \{lobby.getUsername2()}: \{currentCard2.getName()} (\{damage2} Damage) ");
            }
            BattleLog.append("\ndraw");
            lobby.setGameStatus(0);
            lobby.setBattleLog(BattleLog.toString());
            user1.setWin(user1.getDraw()+1);
            user2.setWin(user2.getDraw()+1);
            userRepository.updateUser(user1);
            userRepository.updateUser(user2);
        }

        private double getMultiplier(Card card1, Card card2) {
            //0 fire
            //1 Water
            //2 Regular
            int type1 = card1.getType();
            int type2 = card2.getType();
            if(type1==0 && type2==0)
                return 1;
            int damageType1 = card1.getDamageType();
            int damageType2 = card2.getDamageType();
            if(damageType1==0){
                if(damageType2==1)
                    return 0.5;
                if (damageType2==2)
                    return 2;
            }
            if(damageType1==1){
                if(damageType2==0)
                    return 2;
                if(damageType2==2)
                    return 0.5;
            }
            if(damageType1==2){
                if(damageType2==0)
                    return 0.5;
                if(damageType2==1)
                    return 2;
            }
            return 1;
        }

    }
