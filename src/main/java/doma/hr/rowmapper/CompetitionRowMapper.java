package doma.hr.rowmapper;

import doma.hr.model.Competition;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompetitionRowMapper implements RowMapper<Competition> {
    @Override
    public Competition mapRow(ResultSet rs, int rowNum) throws SQLException {
        Competition competition = new Competition();

        competition.setId(rs.getInt("ID"));
        competition.setCategory(rs.getString("CATEGORY"));
        competition.setYear(rs.getInt("YEAR"));
        competition.setDetails(rs.getString("DETAILS"));
        competition.setRounds(rs.getInt("ROUNDS"));
        competition.setEntryFrom(rs.getDate("ENTRY_FROM"));
        competition.setEntryTo(rs.getDate("ENTRY_TO"));
        competition.setStartDate(rs.getDate("START_DATE"));
        competition.setEndDate(rs.getDate("END_DATE"));

        return competition;
    }
}