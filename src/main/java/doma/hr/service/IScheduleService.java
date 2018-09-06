package doma.hr.service;

public interface IScheduleService {

    String getName();

    void generateGroupSchedule(Integer competitionId) throws Exception;
}
