package at.fhtw.mtcg.helper;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.model.User;

public class TokenVerification {
    public User verifyToken(Request request){
        UnitOfWork unitOfWork = new UnitOfWork();
        try(unitOfWork) {
            UserRepository userRepository = new UserRepository(unitOfWork);
            unitOfWork.commitTransaction();
            return userRepository.getUserByToken(request.getHeaderMap().getHeader("Authorization"));
        }catch (Exception e){
            unitOfWork.rollbackTransaction();
            throw new RuntimeException(e);
        }
    }
}
