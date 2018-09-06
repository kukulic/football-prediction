package doma.hr.validation;

import doma.hr.constants.WorldCup;
import doma.hr.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SchedulerValidation {

    private CompetitionRepository competitionRepository;

    @Autowired
    public SchedulerValidation(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    public boolean groupSchedulerValidation(Integer competitionId) throws Exception {
        //check if competition is already generated
        if (!competitionRepository.isCompetitionExist(competitionId))
            throw new Exception("Competition doesn't exists");
        if (!competitionRepository.numberOfTeamsInCompetition(competitionId).equals(WorldCup.NUMBER_OF_TEAMS_IN_COMPETITION))
            throw new Exception("Wrong number of teams in competition");
        if (!competitionRepository.numberOfGroupsInCompetition(competitionId).equals(WorldCup.NUMBER_OF_GROUPS))
            throw new Exception("Wrong number of groups in competition");
        for (Integer numberInGroup : competitionRepository.numberOfTeamsInEachGroup(competitionId)) {
            if (!numberInGroup.equals(WorldCup.NUMBER_OF_TEAMS_IN_GROUP))
                throw new Exception("Wrong number of teams in group");

        }
        return true;
    }
}
