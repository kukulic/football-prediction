package doma.hr.repository;

import java.util.List;

public interface ITeamRepository {

    List<String> getAllTeams(String category);

    void addTeam(String team, String category);
}
