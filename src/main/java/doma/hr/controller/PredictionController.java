package doma.hr.controller;

import doma.hr.model.Match;
import doma.hr.repository.impl.PredictionRepository;
import doma.hr.service.impl.PredicitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class PredictionController {

    private static final Logger log = LoggerFactory.getLogger(PredictionController.class);

    private PredictionRepository predictionRepository;
    private PredicitionService predicitionService;

    @Autowired
    public PredictionController(PredictionRepository predictionRepository,
                                PredicitionService predicitionService) {
        this.predictionRepository = predictionRepository;
        this.predicitionService = predicitionService;
    }

    @GetMapping("/getAllAvailableMatches")
    public List<Match> getAllAvailableMatches(@RequestParam (value = "competition_id") Integer competitionId,
                                                       @RequestParam (value = "username") String username) {

        return predictionRepository.getAllAvailableMatches(competitionId, username);
    }

    @GetMapping("/getAllFinishedMatches")
    public List<Match> getAllFinishedMatches() {
        return predictionRepository.getAllFinishedMatches();
    }

    @GetMapping("/getAllHistoryMatches")
    public List<Match> getAllHistoryMatches(@RequestParam (value = "competition_id") Integer competitionId,
                                            @RequestParam (value = "username") String username) {
        return predictionRepository.getAllHistoryMatches(competitionId, username);
    }

    @PostMapping("/savePrediction")
    public Match savePrediction(@RequestBody Match matchPrediction) {
        return predicitionService.savePrediction(matchPrediction);
    }

}
