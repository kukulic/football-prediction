package doma.hr.repository;

import doma.hr.model.Team;

import java.util.Date;
import java.util.List;

public interface IScheduleRepository {

    boolean insertMatch(Integer competitionId, String team1, String team2);

    Date getMatchTime(Integer matchId);

    boolean insertMatchTime(Integer matchId, Date matchTime);

    List<Team> getAllTeamsByGroup(Integer competitionId);
}
