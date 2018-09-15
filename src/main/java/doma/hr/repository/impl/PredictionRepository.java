package doma.hr.repository.impl;

import doma.hr.model.Match;
import doma.hr.repository.IPredictionRepository;
import doma.hr.rowmapper.PredictionResultRowMapper;
import doma.hr.rowmapper.PredictionRowMapper;
import doma.hr.rowmapper.UserPredictionRowMapper;
import doma.hr.util.ConvertDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PredictionRepository implements IPredictionRepository {

    private static final Logger log = LoggerFactory.getLogger(PredictionRepository.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PredictionRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
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

    @Override
    public List<Match> getAllFinishedMatches() {

        String query = "SELECT s.match_id, s.team1, s.team2, s.match_time FROM schedule s\n" +
                "WHERE match_time < current_timestamp AND s.match_id NOT IN (SELECT r.match_id FROM result r)";

        return jdbcTemplate.query(query, new BeanPropertyRowMapper(Match.class));
    }

    @Override
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

    @Override
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

    @Override
    public Match insertPrediction(Match matchPrediction){
        String query = "INSERT INTO prediction (match_id, username, team1_goals, team2_goals, winner, insert_time)\n" +
                "VALUES (:match_id, :username, :team1_goals, :team2_goals, :winner, :time); ";



        Map<String,Object> params = this.mapPredictionParams(matchPrediction);
        params.put("time", ConvertDate.gmttoLocalDate(new Date()));

        namedParameterJdbcTemplate.update(query, params);
        return matchPrediction;
    }

    @Override
    public Match updatePrediction(Match matchPrediction){
        String query = "UPDATE prediction SET team1_goals = :team1_goals, \n" +
                "team2_goals = :team2_goals, winner = :winner, update_time = current_timestamp  \n" +
                "WHERE match_id = :match_id AND username = :username";

        Map<String,Object> params = this.mapPredictionParams(matchPrediction);

        namedParameterJdbcTemplate.update(query, params);
        return matchPrediction;
    }

    @Override
    public boolean isMatchPredictionExist(Match matchPrediction){
        String query = "SELECT COUNT(*) FROM prediction \n" +
                "WHERE match_id = :match_id AND username = :username";

        Map<String,Object> params = new HashMap<>();
        params.put("match_id", matchPrediction.getMatchId());
        params.put("username", matchPrediction.getUsername());;

        return (namedParameterJdbcTemplate.queryForObject(query, params, Integer.class) == 1);
    }

    @Override
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

    @Override
    public void insertMatchScore(Integer matchId, String username, BigDecimal score) {

        String query = "UPDATE prediction SET score = :score \n" +
                "WHERE match_id = :matchId AND username = :username";

        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);
        params.put("username", username);
        params.put("score", score);

        namedParameterJdbcTemplate.update(query, params);
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
