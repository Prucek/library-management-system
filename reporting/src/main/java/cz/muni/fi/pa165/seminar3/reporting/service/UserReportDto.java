package cz.muni.fi.pa165.seminar3.reporting.service;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents DTO for user report.
 *
 * @author Peter Rúček
 */
@Getter
@Setter
public class UserReportDto extends ReportDto {

    private int usersCount;

    private int librarianCount;

    private int newUserCount;
}
