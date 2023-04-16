package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for an individual Book instance. Data Transfer Object that is stable for API,
 * independent of internal BookInstance class.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class BookInstanceDto extends DomainObjectDto {
}