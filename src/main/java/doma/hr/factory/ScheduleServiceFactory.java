package doma.hr.factory;

import doma.hr.repository.CompetitionRepository;
import doma.hr.repository.ScheduleRepository;
import doma.hr.service.IScheduleService;
import doma.hr.service.implementation.ScheduleChampionsLeagueService;
import doma.hr.service.implementation.ScheduleWorldCupService;
import org.springframework.beans.factory.annotation.Autowired;

public class ScheduleServiceFactory {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    public IScheduleService getScheduleService(String competitionCategory) {
        if (competitionCategory.equalsIgnoreCase("World Cup"))
            return new ScheduleWorldCupService(scheduleRepository, competitionRepository);

        if (competitionCategory.equalsIgnoreCase("Champions League"))
            return new ScheduleChampionsLeagueService(scheduleRepository, competitionRepository);

        return null;
    }
}
