package doma.hr.rowmapper;

import doma.hr.model.Match;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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