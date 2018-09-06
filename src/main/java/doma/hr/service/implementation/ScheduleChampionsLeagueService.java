package doma.hr.service.implementation;

import doma.hr.model.Match;
import doma.hr.model.Team;
import doma.hr.repository.CompetitionRepository;
import doma.hr.repository.ScheduleRepository;
import doma.hr.service.IScheduleService;
import doma.hr.validation.SchedulerValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleChampionsLeagueService implements IScheduleService {

    private ScheduleRepository scheduleRepository;
    private CompetitionRepository competitionRepository;

    @Autowired
    public ScheduleChampionsLeagueService(ScheduleRepository scheduleRepository,
                                          CompetitionRepository competitionRepository) {
        this.scheduleRepository = scheduleRepository;
        this.competitionRepository = competitionRepository;
    }

    @Override
    public String getName() {
        return "Champions League";
    }

    public void generateGroupSchedule(Integer competitionId) throws Exception{

        SchedulerValidation schedulerValidation = new SchedulerValidation(competitionRepository);
        if (schedulerValidation.groupSchedulerValidation(competitionId)) {

            List<Team> allTeams = scheduleRepository.getAllTeamsByGroup(competitionId);

            for (Team team1 : allTeams) {
                for (Team team2 : allTeams) {
                    if (team1.getGroup().equals(team2.getGroup()) && (team1.getPosition() != team2.getPosition())) {
                        Match match = new Match();

                        match.setTeam1(team1.getName());
                        match.setTeam2(team2.getName());

                        if (!scheduleRepository.insertMatch(competitionId, team1.getName(), team2.getName()))
                            throw new Exception("There is error on generating schedule");
                    }
                }
            }
        }
    }

}
