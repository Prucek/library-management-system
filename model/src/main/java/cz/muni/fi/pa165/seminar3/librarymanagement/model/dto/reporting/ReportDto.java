package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents common properties in report DTO.
 *
 * @author Marek Miček
 */
@Getter
@Setter
public class ReportDto {

    private Instant generatedAt;
}
