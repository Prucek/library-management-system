package cz.muni.fi.pa165.seminar3.reporting.service;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;


/**
 * @brief Represents DTO for book report
 */
@Getter
@Setter
public class BookReportDto extends ReportDto{

    private UserDto user;

    private int borrowedBooksCount;

    private int returnedBooksCount;
}
