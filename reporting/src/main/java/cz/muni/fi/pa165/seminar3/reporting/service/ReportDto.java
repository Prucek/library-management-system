package cz.muni.fi.pa165.seminar3.reporting.service;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


/**
 * @brief Represents common properties in report DTO
 * @author Marek Miček
 */
@Getter
@Setter
public class ReportDto {

    private Instant generatedAt;
}
