package doma.hr.repository.impl;

import doma.hr.model.Team;
import doma.hr.repository.IScheduleRepository;
import doma.hr.rowmapper.TeamRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ScheduleRepository implements IScheduleRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ScheduleRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public boolean insertMatch(Integer competitionId, String team1, String team2) {
        String query = "INSERT INTO SCHEDULE (competition_id, team1, team2)\n" +
                "VALUES(:competitionId, :team1, :team2)";

        Map<String, Object> params = new HashMap<>();
        params.put("competitionId", competitionId);
        params.put("team1", team1);
        params.put("team2", team2);

        return (namedParameterJdbcTemplate.update(query, params) == 1);
    }

    @Override
    public Date getMatchTime(Integer matchId) {
        String query = "SELECT match_time FROM schedule WHERE match_id = :matchId";

        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);

        return namedParameterJdbcTemplate.queryForObject(query, params, Date.class);
    }

    @Override
    public boolean insertMatchTime(Integer matchId, Date matchTime) {
        String query = "UPDATE SCHEDULE SET match_time = :matchTime\n" +
                "WHERE match_id = :matchId";

        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);
        params.put("matchTime", matchTime);

        return (namedParameterJdbcTemplate.update(query, params) == 1);
    }

    @Override
    public List<Team> getAllTeamsByGroup(Integer competitionId) {
        String query = "SELECT gs.team, gs.group_name, rownum() AS position FROM competition c\n" +
                "INNER JOIN group_stage gs ON gs.competition_id = c.id\n" +
                "WHERE c.id = :competitionId\n" +
                "ORDER BY gs.group_name ";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.query(query, params, new TeamRowMapper());
    }

}
