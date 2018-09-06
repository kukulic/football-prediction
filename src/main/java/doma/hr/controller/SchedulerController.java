package doma.hr.controller;

import doma.hr.factory.ScheduleServiceFactory;
import doma.hr.model.Competition;
import doma.hr.repository.ScheduleRepository;
import doma.hr.service.IScheduleService;
import doma.hr.service.implementation.ScheduleChampionsLeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@CrossOrigin
@RestController
public class SchedulerController {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SchedulerController.class);

    private ScheduleRepository scheduleRepository;

    @Autowired
    public SchedulerController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @PostMapping("/generateSchedule")
    public String generateSchedule(@RequestBody Competition competition) {
        log.info("generiram schedule", competition.getCategory());
        ScheduleServiceFactory scheduleServiceFactory = new ScheduleServiceFactory();
        try {
            IScheduleService scheduleService = scheduleServiceFactory.getScheduleService(competition.getCategory());
            scheduleService.generateGroupSchedule(competition.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "OK";
    }

    //getAllMatches (competition_id)

    @PutMapping("/insertMatchTime")
    public void insertMatchTime(@RequestParam(value="match_id") Integer matchId,
                                @RequestParam Date matchTime) {
        scheduleRepository.insertMatchTime(matchId, matchTime);
    }
}
