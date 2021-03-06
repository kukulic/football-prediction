package doma.hr.repository.impl;

import doma.hr.model.User;
import doma.hr.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository implements IUserRepository {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate,
                          NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String query = "SELECT * FROM user";

        return (List<User>)jdbcTemplate.query(query, new BeanPropertyRowMapper(User.class));
    }

    @Override
    public void registerUser(User user) {
        String query = "INSERT INTO user(username, password, email, first_name, last_name, role) " +
                "VALUES (:username, :password, :email, :firstName, :lastName, :role)";

        Map<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        params.put("email", user.getEmail());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("role", "player");

        namedParameterJdbcTemplate.update(query, params);
    }

    @Override
    public User getUserRole(String username, String password) {
        String query = "SELECT username, password, role FROM user WHERE username = :username AND password = :password";

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        return (User)namedParameterJdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper(User.class));
    }

    @Override
    public User authenticateUser(User user) {
        String query = "SELECT * FROM user WHERE username = :username AND password = :password";

        Map<String, Object> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());

        return (User)namedParameterJdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper(User.class));
    }

    @Override
    public Integer numberOfUserInCompetition(Integer competitionId) {
        String query = "SELECT count(*) FROM competition_entry WHERE competition_id = :competitionId";

        Map<String, Object> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.queryForObject(query, params, Integer.class);
    }

    @Override
    public List<String> usersInCompetition(Integer competitionId) {
        String query = "SELECT username FROM competition_entry WHERE competition_id = :competitionId";

        Map<String, Object> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.queryForList(query, params, String.class);
    }
}
