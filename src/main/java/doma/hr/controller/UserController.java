package doma.hr.controller;

import doma.hr.model.User;
import doma.hr.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        userRepository.registerUser(user);
    }


    @GetMapping("/getUserRole")
    public User getUserRole(@RequestParam(value = "username") String username,
                            @RequestParam(value = "password") String password) {

        return userRepository.getUserRole(username, password);
    }
}
