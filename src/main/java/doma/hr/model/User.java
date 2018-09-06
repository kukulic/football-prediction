package doma.hr.model;

import lombok.Data;

@Data
public class User {

    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String token;
    String role;

}
