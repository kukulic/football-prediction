package doma.hr.repository.impl;

import doma.hr.model.Match;
import doma.hr.repository.IResultRepository;
import doma.hr.rowmapper.ResultRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ResultRepository implements IResultRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ResultRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
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

    @Override
    public Match getResult(Integer matchId) {

        String query = "SELECT r.match_id, s.team1, s.team2, r.team1_goals, r.team2_goals  FROM result r \n" +
                "INNER JOIN schedule s ON s.match_id = r.match_id\n " +
                "WHERE r.match_id = :matchId";

        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);

        return namedParameterJdbcTemplate.queryForObject(query, params, new ResultRowMapper());
    }


}
