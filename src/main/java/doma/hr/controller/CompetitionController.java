package doma.hr.controller;

import doma.hr.model.Competition;
import doma.hr.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class CompetitionController {

    private CompetitionRepository competitionRepository;

    @Autowired
    public CompetitionController(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    @GetMapping("/getAllCompetition")
    public List<Competition> getAllCompetition(@RequestParam(value="category") String category) {
        return competitionRepository.getAllCompetiton(category);
    }

    @GetMapping("/getCompetitionCategory")
    public List<String> getCompetitionCategory() {
        return competitionRepository.getAllCompetitionCategories();
    }


    //addTeamToCompetition
}
