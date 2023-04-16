package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents DTO for finance report.
 *
 * @author Peter Rúček
 */
@Getter
@Setter
public class FinanceReportDto extends ReportDto {

    private UserDto user;

    private int finesCount;

    private double finesTotalPaid;
}
