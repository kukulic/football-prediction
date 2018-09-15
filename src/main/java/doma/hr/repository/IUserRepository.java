package doma.hr.repository;

import doma.hr.model.User;

import java.util.List;

public interface IUserRepository {

    List<User> getAllUsers();

    void registerUser(User user);

    User getUserRole(String username, String password);

    User authenticateUser(User user);

    Integer numberOfUserInCompetition(Integer competitionId);

    List<String> usersInCompetition(Integer competitionId);
}
