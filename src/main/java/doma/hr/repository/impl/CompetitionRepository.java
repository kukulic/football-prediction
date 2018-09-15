package doma.hr.repository.impl;

import doma.hr.model.Competition;
import doma.hr.model.Rank;
import doma.hr.repository.ICompetitionRepository;
import doma.hr.rowmapper.CompetitionRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CompetitionRepository implements ICompetitionRepository {

    private static final Logger log = LoggerFactory.getLogger(CompetitionRepository.class);

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CompetitionRepository(JdbcTemplate jdbcTemplate,
                                 NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<String> getAllCompetitionCategories() {
        String query = "SELECT category FROM category_codebook";

        return jdbcTemplate.queryForList(query, String.class);
    }

    @Override
    public String getCompetitionCategory(Integer competitionId) {
        String query = "SELECT category FROM competition WHERE id =:competitionId";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.queryForObject(query, params, String.class);
    }

    @Override
    public List<Competition> getAllCompetiton() {
        String query = "SELECT * FROM competition";

        return jdbcTemplate.query(query, new CompetitionRowMapper());
    }

    @Override
    public List<Competition> getAllCompetiton(String category) {
        String query = "SELECT * FROM competition WHERE category =:category";

        Map<String,String> params = new HashMap<>();
        params.put("category", category);

        return namedParameterJdbcTemplate.query(query, params, new CompetitionRowMapper());
    }

    @Override
    public List<Competition> getEntryCompetitons(String username) {
        String query = "SELECT * FROM competition c \n" +
                "INNER JOIN competition_entry ce ON ce.competition_id = c.id\n" +
                "WHERE ce.username = :username";

        Map<String,String> params = new HashMap<>();
        params.put("username", username);

        return namedParameterJdbcTemplate.query(query, params, new CompetitionRowMapper());
    }

    @Override
    public List<String> getEntryCompetitionCategories(String username) {
        String query = "SELECT c.category FROM competition_entry ce\n" +
                "INNER JOIN competition c ON c.id = ce.competition_id\n" +
                "WHERE ce.username = :username";

        Map<String,String> params = new HashMap<>();
        params.put("username", username);

        return namedParameterJdbcTemplate.queryForList(query, params, String.class);
    }

    @Override
    public Boolean checkIfExist(String username, Integer competitionId) {
        String query = "SELECT COUNT(*) FROM competition_entry \n" +
                "WHERE competition_id = :competitionId AND username = :username";

        Map<String,Object> params = new HashMap<>();
        params.put("competitionId", competitionId);
        params.put("username", username);

        return (namedParameterJdbcTemplate.queryForObject(query, params, Integer.class) == 1);
    }

    @Override
    public String addUserToCompetition(String username, Integer competitionId) {
        String query = "INSERT INTO competition_entry (competition_id, username)\n" +
                "VALUES (:competitionId, :username)";

        Map<String,Object> params = new HashMap<>();
        params.put("competitionId", competitionId);
        params.put("username", username);

        namedParameterJdbcTemplate.update(query, params);
        return username;
    }

    @Override
    public boolean isCompetitionExist(Integer competitionId) {
        String query = "SELECT COUNT(id) FROM competition WHERE id = :competitionId";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return (namedParameterJdbcTemplate.queryForObject(query, params, Integer.class) == 1);
    }

    @Override
    public Integer getCompetitionId(Integer matchId) {

        String query = "SELECT competition_id FROM schedule WHERE match_id = :matchId";

        Map<String,Integer> params = new HashMap<>();
        params.put("matchId", matchId);

        return namedParameterJdbcTemplate.queryForObject(query, params, Integer.class);
    }

    @Override
    public List<Rank> getCompetitionRanking(Integer competitionId) {

        String query = "SELECT p.username, SUM(p.score) AS score FROM prediction p\n" +
                "INNER JOIN competition_entry ce ON p.username = ce.username\n" +
                "INNER JOIN schedule s ON s.match_id = p.match_id\n" +
                "WHERE ce.competition_id = :competitionId AND s.match_time < current_timestamp\n" +
                "GROUP BY p.username\n" +
                "ORDER BY score DESC";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return (List<Rank>)namedParameterJdbcTemplate.query(query, params,  new BeanPropertyRowMapper(Rank.class));
    }

    @Override
    public Integer numberOfTeamsInCompetition(Integer competitionId) {
        String query = "SELECT COUNT(gs.team) FROM competition c\n" +
                "INNER JOIN group_stage gs ON gs.competition_id = c.id\n" +
                "WHERE c.id = :competitionId";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.queryForObject(query, params, Integer.class);
    }

    @Override
    public Integer numberOfGroupsInCompetition(Integer competitionId) {
        String query = "SELECT COUNT(*) FROM (\n" +
                "    SELECT gs.group_name FROM competition c\n" +
                "    INNER JOIN group_stage gs ON gs.competition_id = c.id\n" +
                "    WHERE c.id = :competitionId\n" +
                "    GROUP BY gs.group_name)";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.queryForObject(query, params, Integer.class);
    }

    @Override
    public List<Integer> numberOfTeamsInEachGroup(Integer competitionId) {
        String query = "SELECT COUNT(gs.group_name) FROM competition c\n" +
                "INNER JOIN group_stage gs ON gs.competition_id = c.id\n" +
                "WHERE c.id = :competitionId\n" +
                "GROUP BY gs.group_name;";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.queryForList(query, params, Integer.class);
    }


}
