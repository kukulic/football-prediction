package doma.hr.repository;

import doma.hr.model.Competition;
import doma.hr.model.Rank;

import java.util.List;

public interface ICompetitionRepository {


    List<String> getAllCompetitionCategories();

    String getCompetitionCategory(Integer competitionId);

    List<Competition> getAllCompetiton();

    List<Competition> getAllCompetiton(String category);

    List<Competition> getEntryCompetitons(String username);

    List<String> getEntryCompetitionCategories(String username);

    Boolean checkIfExist(String username, Integer competitionId);

    String addUserToCompetition(String username, Integer competitionId);

    boolean isCompetitionExist(Integer competitionId);

    Integer getCompetitionId(Integer matchId);

    List<Rank> getCompetitionRanking(Integer competitionId);

    Integer numberOfTeamsInCompetition(Integer competitionId);

    Integer numberOfGroupsInCompetition(Integer competitionId);

    List<Integer> numberOfTeamsInEachGroup(Integer competitionId);
}