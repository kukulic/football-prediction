package doma.hr.controller;

import doma.hr.model.Competition;
import doma.hr.model.UserCompetition;
import doma.hr.repository.CompetitionRepository;
import doma.hr.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class CompetitionController {

    private CompetitionRepository competitionRepository;
    private CompetitionService competitionService;

    @Autowired
    public CompetitionController(CompetitionRepository competitionRepository,
                                 CompetitionService competitionService) {
        this.competitionRepository = competitionRepository;
        this.competitionService = competitionService;
    }

    @GetMapping("/getAllPossibleCompetition")
    public List<Competition> getAllCompetition() {
        return competitionRepository.getAllCompetiton();
    }

    @GetMapping("/getAllCompetition")
    public List<Competition> getAllCompetition(@RequestParam(value="category") String category) {
        return competitionRepository.getAllCompetiton(category);
    }

    @GetMapping("/getEntryCompetitions")
    public List<Competition> getEntryCompetitions(@RequestParam(value="username") String username) {
        return competitionRepository.getEntryCompetitons(username);
    }

    @GetMapping("/getCompetitionCategory")
    public List<String> getCompetitionCategory() {
        return competitionRepository.getAllCompetitionCategories();
    }

    @GetMapping("/getEntryCompetitionCategory")
    public List<String> getEntryCompetitionCategory(@RequestParam(value="username") String username) {
        return competitionRepository.getEntryCompetitionCategories(username);
    }


    @PostMapping("/addUserToCompetition")
    public String addUserToCompetition(@RequestBody UserCompetition userCompetition) {

        return competitionService.addUserToCompetition(userCompetition.getUsername(), userCompetition.getCompetitionId());

    }

    //addTeamToCompetition
}
