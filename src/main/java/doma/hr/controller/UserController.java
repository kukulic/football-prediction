package doma.hr.controller;

import doma.hr.model.User;
import doma.hr.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController("/users")
public class UserController {

    private IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {

        userService.registerUser(user);
    }


    @GetMapping("/getUserRole")
    public User getUserRole(@RequestParam(value = "username") String username,
                            @RequestParam(value = "password") String password) {

        return userService.getUserRole(username, password);
    }

    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {

        return userService.getAllUsers();
    }
}
