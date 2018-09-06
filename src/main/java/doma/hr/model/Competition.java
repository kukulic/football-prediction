package doma.hr.model;

import lombok.Data;

import java.util.Date;

@Data
public class Competition {
    private Integer id;
    private String category;
    private Integer year;
    private String details;
    private Integer rounds;
    private Date entryFrom;
    private Date entryTo;
    private Date startDate;
    private Date endDate;

}