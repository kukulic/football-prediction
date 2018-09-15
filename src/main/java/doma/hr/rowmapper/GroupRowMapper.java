package doma.hr.rowmapper;

import doma.hr.model.Group;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

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