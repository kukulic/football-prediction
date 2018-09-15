package doma.hr.service.impl;

import doma.hr.model.User;
import doma.hr.repository.IUserRepository;
import doma.hr.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(User user) {
        userRepository.registerUser(user);
    }

    @Override
    public User getUserRole(String username, String password) {
        return userRepository.getUserRole(username, password);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = userRepository.getAllUsers();
        userList.forEach(user -> user.setPassword("*****"));

        return userList;
    }
}
