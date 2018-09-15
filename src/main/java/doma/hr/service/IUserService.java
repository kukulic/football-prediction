package doma.hr.service;

import doma.hr.model.User;

import java.util.List;

public interface IUserService {

    void registerUser(User user);

    User getUserRole(String username, String password);

    List<User> getAllUsers();
}
