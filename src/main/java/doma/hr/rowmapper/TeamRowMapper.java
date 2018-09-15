package doma.hr.rowmapper;

import doma.hr.model.Team;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TeamRowMapper implements RowMapper<Team> {
    @Override
    public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
        Team team = new Team();

        team.setName(rs.getString("TEAM"));
        team.setGroup(rs.getString("GROUP_NAME"));
        team.setPosition(rs.getInt("POSITION"));

        return team;
    }
}