package doma.hr.repository;

import doma.hr.model.Match;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ResultRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ResultRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Match insertResult(Match matchResult) {

        String query = "INSERT INTO result (match_id, team1_goals, team2_goals, winner)\n" +
                "VALUES(:matchId, :team1Goals, :team2Goals, :winner)";

        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchResult.getMatchId());
        params.put("team1Goals", matchResult.getTeam1GoalsResult());
        params.put("team2Goals", matchResult.getTeam2GoalsResult());
        params.put("winner", matchResult.getWinnerResult());

        namedParameterJdbcTemplate.update(query, params);

        return matchResult;
    }

    public Match getResult(Integer matchId) {

        String query = "SELECT r.match_id, s.team1, s.team2, r.team1_goals, r.team2_goals  FROM result r \n" +
                "INNER JOIN schedule s ON s.match_id = r.match_id\n " +
                "WHERE r.match_id = :matchId";

        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);

        return namedParameterJdbcTemplate.queryForObject(query, params, new ResultRowMapper());
    }

    public static class ResultRowMapper implements RowMapper<Match> {
        @Override
        public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
            Match match = new Match();

            match.setMatchId(rs.getInt("MATCH_ID"));
            match.setTeam1(rs.getString("TEAM1"));
            if (rs.getString("TEAM1_GOALS") != null)
                match.setTeam1GoalsResult(rs.getInt("TEAM1_GOALS"));
            match.setTeam2(rs.getString("TEAM2"));
            if (rs.getString("TEAM2_GOALS") != null)
                match.setTeam2GoalsResult(rs.getInt("TEAM2_GOALS"));

            return match;
        }
    }
}
