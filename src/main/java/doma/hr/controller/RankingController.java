package doma.hr.controller;

import doma.hr.model.Rank;
import doma.hr.repository.impl.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class RankingController {

    private CompetitionRepository competitionRepository;

    @Autowired
    public RankingController(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    @GetMapping("/getCompetitionRanking")
    public List<Rank>getCompetitionRanking(@RequestParam(value = "competitionId") Integer competitionId) {
        return competitionRepository.getCompetitionRanking(competitionId);
    }

}
