package cz.muni.fi.pa165.seminar3.reporting.service;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

/**
 * @brief Represents DTO for finance report
 */
@Getter
@Setter
public class FinanceReportDto extends ReportDto{

    private UserDto user;

    private int finesCount;

    private double finesTotalPaid;
}
