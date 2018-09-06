package doma.hr.controller;

import doma.hr.Application;
import doma.hr.model.Match;
import doma.hr.repository.CompetitionRepository;
import doma.hr.repository.ResultRepository;
import doma.hr.service.ScoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class ResultController {

    private static final Logger log = LoggerFactory.getLogger(ResultController.class);

    private ResultRepository resultRepository;
    private CompetitionRepository competitionRepository;
    private ScoringService scoringService;

    @Autowired
    public ResultController(ResultRepository resultRepository,
                            CompetitionRepository competitionRepository,
                            ScoringService scoringService) {
        this.resultRepository = resultRepository;
        this.competitionRepository = competitionRepository;
        this.scoringService = scoringService;
    }

    @PostMapping("/saveResult")
    public Match saveResult(@RequestBody Match matchResult) {
        Integer competitionId;

        Match match = resultRepository.insertResult(matchResult);
        competitionId = competitionRepository.getCompetitionId(matchResult.getMatchId());
        scoringService.calculateMatchScore(matchResult.getMatchId(), competitionId);

        return match;
    }
}
