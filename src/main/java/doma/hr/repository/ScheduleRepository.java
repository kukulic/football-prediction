package doma.hr.repository;

import doma.hr.model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import sun.util.calendar.LocalGregorianCalendar;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class ScheduleRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ScheduleRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public boolean insertMatch(Integer competitionId, String team1, String team2) {
        String query = "INSERT INTO SCHEDULE (competition_id, team1, team2)\n" +
                "VALUES(:competitionId, :team1, :team2)";

        Map<String, Object> params = new HashMap<>();
        params.put("competitionId", competitionId);
        params.put("team1", team1);
        params.put("team2", team2);

        return (namedParameterJdbcTemplate.update(query, params) == 1);

    }

    public Date getMatchTime(Integer matchId) {
        String query = "SELECT match_time FROM schedule WHERE match_id = :matchId";

        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);


        return namedParameterJdbcTemplate.queryForObject(query, params, Date.class);
    }

    public boolean insertMatchTime(Integer matchId, Date matchTime) {
        String query = "UPDATE SCHEDULE SET match_time = :matchTime\n" +
                "WHERE match_id = :matchId";

        Map<String, Object> params = new HashMap<>();
        params.put("matchId", matchId);
        params.put("matchTime", matchTime);

        return (namedParameterJdbcTemplate.update(query, params) == 1);
    }

    public List<Team> getAllTeamsByGroup(Integer competitionId) {
        String query = "SELECT gs.team, gs.group_name, rownum() AS position FROM competition c\n" +
                "INNER JOIN group_stage gs ON gs.competition_id = c.id\n" +
                "WHERE c.id = :competitionId\n" +
                "ORDER BY gs.group_name ";

        Map<String,Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);

        return namedParameterJdbcTemplate.query(query, params, new TeamRowMapper());
    }

    public class TeamRowMapper implements RowMapper<Team> {
        @Override
        public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
            Team team = new Team();

            team.setName(rs.getString("TEAM"));
            team.setGroup(rs.getString("GROUP_NAME"));
            team.setPosition(rs.getInt("POSITION"));

            return team;
        }
    }
}
