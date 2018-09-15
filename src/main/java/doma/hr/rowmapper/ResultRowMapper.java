package doma.hr.rowmapper;

import doma.hr.model.Match;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultRowMapper implements RowMapper<Match> {
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