package doma.hr.repository;

import doma.hr.model.Match;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IPredictionRepository {

    List<Match> getAllMatches(String username, Integer competitionId);

    List<Match> getAllFinishedMatches();

    List<Match> getAllAvailableMatches(Integer competitionId, String username);

    List<Match> getAllHistoryMatches(Integer competitionId, String username);

    Match insertPrediction(Match matchPrediction);

    Match updatePrediction(Match matchPrediction);

    boolean isMatchPredictionExist(Match matchPrediction);

    Match getUserMatchPrediction(Integer matchId, String username);

    void insertMatchScore(Integer matchId, String username, BigDecimal score);

    Map<String,Object> mapPredictionParams(Match matchPrediction);
}
