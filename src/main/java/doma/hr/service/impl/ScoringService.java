package doma.hr.service.impl;

import doma.hr.model.Match;
import doma.hr.repository.impl.PredictionRepository;
import doma.hr.repository.impl.ResultRepository;
import doma.hr.repository.impl.UserRepository;
import doma.hr.service.IScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoringService implements IScoringService {

    private static final BigDecimal WINNER_HIT = new BigDecimal(1);
    private static final BigDecimal RESULT_HIT = new BigDecimal(2);
    private static final BigDecimal WINNER_HIT_PERCENTAGE_BONUS = new BigDecimal(0.2); // 20%
    private static final BigDecimal RESULT_HIT_PERCENTAGE_BONUS = new BigDecimal(0.1); // 10%

    private UserRepository userRepository;
    private ResultRepository resultRepository;
    private PredictionRepository predictionRepository;

    @Autowired
    public ScoringService(UserRepository userRepository,
                          ResultRepository resultRepository,
                          PredictionRepository predictionRepository) {
        this.userRepository = userRepository;
        this.resultRepository = resultRepository;
        this.predictionRepository = predictionRepository;
    }

    @Override
    public void calculateMatchScore(Integer matchId, Integer competitionId) {

        Integer numberOfUsers  = userRepository.numberOfUserInCompetition(competitionId);
        List<String> userList = userRepository.usersInCompetition(competitionId);
        Match match = resultRepository.getResult(matchId);
        HashMap<String, BigDecimal> map = new HashMap<>();
        HashMap<String, BigDecimal> mapResult = new HashMap<>();
        BigDecimal winnerHitBonus = new BigDecimal(0);
        BigDecimal resultHitBonus = new BigDecimal(0);

        if (match.getTeam1GoalsResult() > match.getTeam2GoalsResult())
            match.setWinnerResult(match.getTeam1());
        if (match.getTeam1GoalsResult() < match.getTeam2GoalsResult())
            match.setWinnerResult(match.getTeam2());
        if (match.getTeam1GoalsResult() == match.getTeam2GoalsResult())
            match.setWinnerResult("Draw");

        userList.forEach(username -> {
            Match userMatchPrediction = predictionRepository.getUserMatchPrediction(matchId, username);
            if (userMatchPrediction != null && match.getWinnerResult().equals(userMatchPrediction.getWinnerPrediction())) {
                map.put(username, WINNER_HIT);
                if (match.getTeam1GoalsResult().equals(userMatchPrediction.getTeam1GoalsPrediction()) &&
                        match.getTeam2GoalsResult().equals(userMatchPrediction.getTeam2GoalsPrediction())) {
                    BigDecimal score = map.get(username);
                    // adding result hit to original map
                    map.replace(username, score.add(RESULT_HIT));
                    mapResult.put(username, RESULT_HIT);
                }
            }
            predictionRepository.insertMatchScore(matchId, username, new BigDecimal(0).setScale(2, RoundingMode.CEILING));
        });

        if (map.size() != 0)
            winnerHitBonus = WINNER_HIT.multiply(new BigDecimal(numberOfUsers))
                    .multiply(WINNER_HIT_PERCENTAGE_BONUS)
                    .divide(new BigDecimal(map.size()), 2, RoundingMode.HALF_UP);
        if (mapResult.size() != 0)
            resultHitBonus = RESULT_HIT.multiply(new BigDecimal(numberOfUsers))
                    .multiply(RESULT_HIT_PERCENTAGE_BONUS)
                    .divide(new BigDecimal(mapResult.size()), 2, RoundingMode.HALF_UP);

        for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
            if (mapResult.containsKey(entry.getKey()))
                predictionRepository.insertMatchScore(matchId, entry.getKey(), entry.getValue().add(winnerHitBonus).add(resultHitBonus).setScale(2, RoundingMode.CEILING));
            else
                predictionRepository.insertMatchScore(matchId, entry.getKey(), entry.getValue().add(winnerHitBonus).setScale(2, RoundingMode.CEILING));
        }
    }

    @Override
    public void calclulateRoundScore() {

    }

    @Override
    public void calculateCompetitionScore() {

    }
}
