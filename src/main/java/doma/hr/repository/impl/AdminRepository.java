package doma.hr.repository.impl;

import doma.hr.model.User;
import doma.hr.repository.IAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminRepository implements IAdminRepository {

    private static final Logger log = LoggerFactory.getLogger(AdminRepository.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AdminRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUnconfirmedUsers() {
        return null;
    }

}
