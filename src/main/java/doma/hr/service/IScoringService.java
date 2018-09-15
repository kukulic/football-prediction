package doma.hr.service;

public interface IScoringService {

    void calculateMatchScore(Integer matchId, Integer competitionId);

    void calclulateRoundScore();

    void calculateCompetitionScore();
}
