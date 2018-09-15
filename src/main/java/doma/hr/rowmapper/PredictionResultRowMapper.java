package doma.hr.rowmapper;

import doma.hr.model.Match;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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