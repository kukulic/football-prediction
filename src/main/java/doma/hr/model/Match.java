package doma.hr.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Match {

    private Integer matchId;
    private String team1;
    private String team2;
    private Timestamp matchTime;
    private String username;
    private Integer team1GoalsPrediction;
    private Integer team2GoalsPrediction;
    private String winnerPrediction;
    private Integer team1GoalsResult;
    private Integer team2GoalsResult;
    private String winnerResult;
    private BigDecimal userScore;

}
