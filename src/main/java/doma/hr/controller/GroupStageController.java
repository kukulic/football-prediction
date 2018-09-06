package doma.hr.controller;

import doma.hr.model.Group;
import doma.hr.repository.GroupStageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class GroupStageController {

    private GroupStageRepository groupStageRepository;

    @Autowired
    public GroupStageController(GroupStageRepository groupStageRepository) {
        this.groupStageRepository = groupStageRepository;
    }

    @GetMapping("/getAllGroups")
    public List<Group> getAllGroups(@RequestParam(value = "competitionId") Integer competitionId) {
        return groupStageRepository.getAllGroups(competitionId);
    }

    @GetMapping("/getGroup")
    public List<Group> getGroup(@RequestParam(value = "competitionId") Integer competitionId,
                                @RequestParam(value = "group") String group) {
        return groupStageRepository.getGroup(competitionId, group);
    }

    //addTeamToGroup(team, competition, group)

    //getAvailableGroups

    //add constants - A B ...

    //removeTeamFromGroup(team, competition, group)
}
