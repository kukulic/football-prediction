package doma.hr.controller;

import doma.hr.model.User;
import doma.hr.repository.impl.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class AuthenticateController {

    private UserRepository userRepository;

    @Autowired
    public AuthenticateController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/authenticate")
    public User authenticateUser(@RequestBody User user){

        User authUser = userRepository.authenticateUser(user);

        if (authUser != null) {
            authUser.setToken("123456");
            return authUser;
        }
        return null;
    }
}
