package doma.hr.repository;

import doma.hr.Application;
import doma.hr.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PredictionRepository {

    private static final Logger log = LoggerFactory.getLogger(PredictionRepository.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PredictionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Match> getAllMatches(String username, Integer competitionId) {

        String query = "SELECT s.match_id, s.team1, s.team2, s.match_time FROM schedule s\n" +
                "INNER JOIN competition_entry ce ON s.competition_id = ce.competition_id\n" +
                "WHERE ce.username = :username\n" +
                "AND ce.competition_id = :competitionId\n" +
                "ORDER BY s.match_time";

        Map<String,Object> params = new HashMap<>();
        params.put("username", username);
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.queryForList(query, params, Match.class);
    }

    public List<Match> getAllFinishedMatches() {

        String query = "SELECT s.match_id, s.team1, s.team2, s.match_time FROM schedule s\n" +
                "WHERE match_time < current_timestamp AND s.match_id NOT IN (SELECT r.match_id FROM result r)";

        return jdbcTemplate.query(query, new BeanPropertyRowMapper(Match.class));
    }

    public List<Match> getAllAvailableMatches(Integer competitionId, String username) {

        String query = "SELECT s.match_ID, s.team1, p.team1_goals AS team1_goals_pred, s.team2,\n" +
                "p.team2_goals AS team2_goals_pred, p.winner AS winner_pred, s.match_time FROM schedule s\n" +
                "INNER JOIN competition_entry ce ON s.competition_id = ce.competition_id\n" +
                "LEFT JOIN prediction p ON p.match_id = s.match_id AND ce.username = p.username\n" +
                "WHERE ce.username = :username\n" +
                "AND s.match_time > current_timestamp\n" +
                "AND ce.competition_id = :competitionId\n" +
                "ORDER BY s.match_time";

        Map<String,Object> params = new HashMap<>();
        params.put("username", username);
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.query(query, params, new PredictionRowMapper());
    }

    public List<Match> getAllHistoryMatches(Integer competitionId, String username) {

        String query = "SELECT s.match_id, s.team1, p.team1_goals AS team1_goals_pred, s.team2,\n " +
                "p.team2_goals AS team2_goals_pred, p.winner AS winner_pred, r.team1_goals AS team1_goals_res,\n" +
                "r.team2_goals AS team2_goals_res, r.winner AS winner_res, s.match_time, p.score FROM schedule s\n" +
                "INNER JOIN competition_entry ce ON s.competition_id = ce.competition_id\n" +
                "LEFT JOIN result r ON s.match_id = r.match_id  \n" +
                "LEFT JOIN prediction p ON p.match_id = s.match_id AND ce.username = p.username\n" +
                "WHERE ce.username = :username\n" +
                "AND s.match_time < current_timestamp\n" +
                "AND ce.competition_id = :competitionId\n" +
                "ORDER BY s.match_time";

        Map<String,Object> params = new HashMap<>();
        params.put("username", username);
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.query(query, params, new PredictionResultRowMapper());
    }

    public Match insertPrediction(Match matchPrediction){
        String query = "INSERT INTO prediction (match_id, username, team1_goals, team2_goals, winner, insert_time)\n" +
                "VALUES (:match_id, :username, :team1_goals, :team2_goals, :winner, current_timestamp); ";

        Map<String,Object> params = this.mapPredictionParams(matchPrediction);

        namedParameterJdbcTemplate.update(query, params);
        return matchPrediction;
    }

    public Match updatePrediction(Match matchPrediction){
        String query = "UPDATE prediction SET team1_goals = :team1_goals, \n" +
                "team2_goals = :team2_goals, winner = :winner, update_time = current_timestamp  \n" +
                "WHERE match_id = :match_id AND username = :username";

        Map<String,Object> params = this.mapPredictionParams(matchPrediction);

        namedParameterJdbcTemplate.update(query, params);
        return matchPrediction;
    }

    public boolean isMatchPredictionExist(Match matchPrediction){
        String query = "SELECT COUNT(*) FROM prediction \n" +
                "WHERE match_id = :match_id AND username = :username";

        Map<String,Object> params = new HashMap<>();
        params.put("match_id", matchPrediction.getMatchId());
        params.put("username", matchPrediction.getUsername());;

        return (namedParameterJdbcTemplate.queryForObject(query, params, Integer.class) == 1);
    }

    public Match getUserMatchPrediction(Integer matchId, String username) {
        Match match = new Match();

        String query = "SELECT match_id, team1_goals AS team1_goals_pred, team2_goals AS team2_goals_pred," +
                "winner AS winner_pred FROM prediction\n" +
                "WHERE match_id = :matchId AND username = :username";

        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);
        params.put("username", username);


        try {
            match = namedParameterJdbcTemplate.queryForObject(query, params, new UserPredictionRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            log.info("User: {} didn't put prediction for this game: {}", username , matchId);
        }

        return match;
    }

    public void insertMatchScore(Integer matchId, String username, BigDecimal score) {

        String query = "UPDATE prediction SET score = :score \n" +
                "WHERE match_id = :matchId AND username = :username";

        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);
        params.put("username", username);
        params.put("score", score);

        namedParameterJdbcTemplate.update(query, params);
    }

    public class UserPredictionRowMapper implements RowMapper<Match> {
        @Override
        public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
            Match match = new Match();

            match.setMatchId(rs.getInt("MATCH_ID"));
            if (rs.getString("TEAM1_GOALS_PRED") != null)
                match.setTeam1GoalsPrediction(rs.getInt("TEAM1_GOALS_PRED"));
            if (rs.getString("TEAM2_GOALS_PRED") != null)
                match.setTeam2GoalsPrediction(rs.getInt("TEAM2_GOALS_PRED"));
            match.setWinnerPrediction(rs.getString("WINNER_PRED"));

            return match;
        }
    }

    public class PredictionRowMapper implements RowMapper<Match> {
        @Override
        public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
            Match match = new Match();

            match.setMatchId(rs.getInt("MATCH_ID"));
            if (rs.getString("TEAM1") != null)
                match.setTeam1(rs.getString("TEAM1"));
            if (rs.getString("TEAM1_GOALS_PRED") != null)
                match.setTeam1GoalsPrediction(rs.getInt("TEAM1_GOALS_PRED"));
            if (rs.getString("TEAM2") != null)
                match.setTeam2(rs.getString("TEAM2"));
            if (rs.getString("TEAM2_GOALS_PRED") != null)
                match.setTeam2GoalsPrediction(rs.getInt("TEAM2_GOALS_PRED"));
            match.setWinnerPrediction(rs.getString("WINNER_PRED"));
            match.setMatchTime(rs.getTimestamp("MATCH_TIME"));

            return match;
        }
    }

    public class PredictionResultRowMapper implements RowMapper<Match> {
        @Override
        public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
            Match match = new Match();

            match.setMatchId(rs.getInt("MATCH_ID"));
            match.setTeam1(rs.getString("TEAM1"));
            if (rs.getString("TEAM1_GOALS_PRED") != null)
                match.setTeam1GoalsPrediction(rs.getInt("TEAM1_GOALS_PRED"));
            match.setTeam2(rs.getString("TEAM2"));
            if (rs.getString("TEAM2_GOALS_PRED") != null)
                match.setTeam2GoalsPrediction(rs.getInt("TEAM2_GOALS_PRED"));
            match.setWinnerPrediction(rs.getString("WINNER_PRED"));
            if (rs.getString("TEAM1_GOALS_RES") != null)
                match.setTeam1GoalsResult(rs.getInt("TEAM1_GOALS_RES"));
            match.setTeam2(rs.getString("TEAM2"));
            if (rs.getString("TEAM2_GOALS_RES") != null)
                match.setTeam2GoalsResult(rs.getInt("TEAM2_GOALS_RES"));
            if (rs.getString("WINNER_RES") != null)
                match.setWinnerResult(rs.getString("WINNER_RES"));
            if (rs.getString("SCORE") != null)
                match.setUserScore(rs.getBigDecimal("SCORE"));
            match.setMatchTime(rs.getTimestamp("MATCH_TIME"));

            return match;
        }
    }


    public Map<String,Object> mapPredictionParams(Match matchPrediction) {
        Map<String,Object> params = new HashMap<>();
        params.put("match_id", matchPrediction.getMatchId());
        params.put("username", matchPrediction.getUsername());
        params.put("team1_goals", matchPrediction.getTeam1GoalsPrediction());
        params.put("team2_goals", matchPrediction.getTeam2GoalsPrediction());
        params.put("winner", matchPrediction.getWinnerPrediction());

        return params;
    }


}
