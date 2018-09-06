package doma.hr.controller;

import doma.hr.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class TeamController {

    private TeamRepository teamRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @PostMapping("/addTeam")
    public void addTeam(@RequestParam(value="team") String team,
                        @RequestParam(value="category") String category) {
        teamRepository.addTeam(team, category);
    }

    @GetMapping("/getAllTeams")
    public List<String> getAllTeams(@RequestParam(value="category") String category) {
        return teamRepository.getAllTeams(category);
    }

    //getAllTeamByCompetition

    //getAllCategories


}
