package doma.hr.repository;

import doma.hr.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GroupStageRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public GroupStageRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Group> getAllGroups(Integer competitionId) {
        String query = "SELECT * FROM group_stage WHERE competition_id = :competitionId";

        Map<String, Integer> params = new HashMap<>();
        params.put("competitionId", competitionId);


        return namedParameterJdbcTemplate.query(query, params, new GroupStageRepository.GroupRowMapper());
    }

    public List<Group> getGroup(Integer competitionId, String group) {
        String query = "SELECT * FROM group_stage WHERE competition_id =:competitionId AND group_name = :group";

        Map<String, Object> params = new HashMap<>();
        params.put("competitionId", competitionId);
        params.put("group", group);

        return namedParameterJdbcTemplate.query(query, params, new GroupStageRepository.GroupRowMapper());
    }

    public class GroupRowMapper implements RowMapper<Group> {
        @Override
        public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
            Group group = new Group();

            group.setCompetitionId(rs.getInt("COMPETITION_ID"));
            group.setGroup(rs.getString("GROUP_NAME"));
            group.setPosition(rs.getInt("POSITION"));
            group.setTeam(rs.getString("TEAM"));

            return group;
        }
    }
}
