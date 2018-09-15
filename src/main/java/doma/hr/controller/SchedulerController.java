package doma.hr.controller;

import doma.hr.factory.ScheduleServiceFactory;
import doma.hr.model.Competition;
import doma.hr.repository.impl.ScheduleRepository;
import doma.hr.service.IScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@CrossOrigin
@RestController
public class SchedulerController {

    private static final Logger log = LoggerFactory.getLogger(SchedulerController.class);

    private ScheduleRepository scheduleRepository;
    private ScheduleServiceFactory scheduleServiceFactory;

    @Autowired
    public SchedulerController(ScheduleRepository scheduleRepository,
                               ScheduleServiceFactory scheduleServiceFactory) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleServiceFactory = scheduleServiceFactory;
    }

    @PostMapping("/generateSchedule")
    public String generateSchedule(@RequestBody Competition competition) {
        log.info("generiram schedule", competition.getCategory());
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
