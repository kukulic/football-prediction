package doma.hr.repository;

import doma.hr.Application;
import doma.hr.model.Competition;
import doma.hr.model.Rank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CompetitionRepository {

    private static final Logger log = LoggerFactory.getLogger(CompetitionRepository.class);

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CompetitionRepository(JdbcTemplate jdbcTemplate,
                                 NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<String> getAllCompetitionCategories() {
        String query = "SELECT category FROM category_codebook";

        return jdbcTemplate.queryForList(query, String.class);
    }

    public String getCompetitionCategory(Integer competitionId) {
        String query = "SELECT category FROM competition WHERE id =:competitionId";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.queryForObject(query, params, String.class);
    }

    public List<Competition> getAllCompetiton(String category) {
        String query = "SELECT * FROM competition WHERE category =:category";

        Map<String,String> params = new HashMap<>();
        params.put("category", category);


        return namedParameterJdbcTemplate.query(query, params, new CompetitionRowMapper());
    }

    public boolean isCompetitionExist(Integer competitionId) {
        String query = "SELECT COUNT(id) FROM competition WHERE id = :competitionId";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return (namedParameterJdbcTemplate.queryForObject(query, params, Integer.class) == 1);
    }

    public Integer getCompetitionId(Integer matchId) {

        String query = "SELECT competition_id FROM schedule WHERE match_id = :matchId";

        Map<String,Integer> params = new HashMap<>();
        params.put("matchId", matchId);

        return namedParameterJdbcTemplate.queryForObject(query, params, Integer.class);
    }

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

    public Integer numberOfTeamsInCompetition(Integer competitionId) {
        String query = "SELECT COUNT(gs.team) FROM competition c\n" +
                "INNER JOIN group_stage gs ON gs.competition_id = c.id\n" +
                "WHERE c.id = :competitionId";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.queryForObject(query, params, Integer.class);
    }

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

    public List<Integer> numberOfTeamsInEachGroup(Integer competitionId) {
        String query = "SELECT COUNT(gs.group_name) FROM competition c\n" +
                "INNER JOIN group_stage gs ON gs.competition_id = c.id\n" +
                "WHERE c.id = :competitionId\n" +
                "GROUP BY gs.group_name;";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.queryForList(query, params, Integer.class);
    }

    public class CompetitionRowMapper implements RowMapper<Competition> {
        @Override
        public Competition mapRow(ResultSet rs, int rowNum) throws SQLException {
            Competition competition = new Competition();

            competition.setId(rs.getInt("ID"));
            competition.setCategory(rs.getString("CATEGORY"));
            competition.setYear(rs.getInt("YEAR"));
            competition.setDetails(rs.getString("DETAILS"));
            competition.setRounds(rs.getInt("ROUNDS"));
            competition.setEntryFrom(rs.getDate("ENTRY_FROM"));
            competition.setEntryTo(rs.getDate("ENTRY_TO"));
            competition.setStartDate(rs.getDate("START_DATE"));
            competition.setEndDate(rs.getDate("END_DATE"));

            return competition;
        }
    }
}
