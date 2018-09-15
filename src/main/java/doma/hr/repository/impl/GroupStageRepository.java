package doma.hr.repository.impl;

import doma.hr.model.Group;
import doma.hr.repository.IGroupStageRepository;
import doma.hr.rowmapper.GroupRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GroupStageRepository implements IGroupStageRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public GroupStageRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Group> getAllGroups(Integer competitionId) {
        String query = "SELECT * FROM group_stage WHERE competition_id = :competitionId";

        Map<String, Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);


        return namedParameterJdbcTemplate.query(query, params, new GroupRowMapper());
    }

    @Override
    public List<Group> getGroup(Integer competitionId, String group) {
        String query = "SELECT * FROM group_stage WHERE competition_id =:competitionId AND group_name = :group";

        Map<String, Object> params = new HashMap<>();
        params.put("competitionId", competitionId);
        params.put("group", group);

        return namedParameterJdbcTemplate.query(query, params, new GroupRowMapper());
    }

}
