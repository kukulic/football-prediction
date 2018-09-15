package doma.hr.repository;

import doma.hr.model.Group;
import java.util.List;

public interface IGroupStageRepository {

    List<Group> getAllGroups(Integer competitionId);

    List<Group> getGroup(Integer competitionId, String group);
}
