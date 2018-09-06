package doma.hr.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TeamRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public TeamRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<String> getAllTeams(String category) {

        String query="SELECT team FROM team_codebook WHERE category =:category";

        Map<String,Object> params = new HashMap<>();
        params.put("category", category);

        return namedParameterJdbcTemplate.queryForList(query, params, String.class);
    }

    public void addTeam(String team, String category) {

        String query="INSERT INTO team_codebook(team, category) VALUES (:team, :category)";

        Map<String,Object> params = new HashMap<>();
        params.put("team", team);
        params.put("category", category);

        namedParameterJdbcTemplate.update(query, params);
    }
}
