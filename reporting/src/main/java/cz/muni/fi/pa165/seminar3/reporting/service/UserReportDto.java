package cz.muni.fi.pa165.seminar3.reporting.service;

import lombok.Getter;
import lombok.Setter;

/**
 * @brief Represents DTO for user report
 */
@Getter
@Setter
public class UserReportDto extends ReportDto{

    private int usersCount;

    private int librarianCount;

    private int newUserCount;
}
