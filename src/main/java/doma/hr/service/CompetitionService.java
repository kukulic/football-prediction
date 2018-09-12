package doma.hr.service;

import doma.hr.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompetitionService {

    private CompetitionRepository competitionRepository;

    @Autowired
    public CompetitionService(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    public String addUserToCompetition(String username, Integer competitionId) {
        // check if user is already in competition
        if (competitionRepository.checkIfExist(username, competitionId))
            return "User je od ranije u natjecanju";
        competitionRepository.addUserToCompetition(username, competitionId);
        return "OK";
    }
}
