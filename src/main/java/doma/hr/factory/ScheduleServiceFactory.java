package doma.hr.factory;

import doma.hr.service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleServiceFactory {

    private List<IScheduleService> scheduleServiceList;

    @Autowired
    public ScheduleServiceFactory(List<IScheduleService> scheduleServiceList) {
        this.scheduleServiceList = scheduleServiceList;
    }

    public IScheduleService getScheduleService(String competitionCategory) {
    for (IScheduleService service : scheduleServiceList) {
        if (service.getName().equals(competitionCategory))
            return service;
    }
        return null;
    }
}
