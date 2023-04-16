package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;


/**
 * Represents DTO for book report.
 *
 * @author Peter Rúček
 */
@Getter
@Setter
public class BookReportDto extends ReportDto {

    private UserDto user;

    private int borrowedBooksCount;

    private int returnedBooksCount;
}
