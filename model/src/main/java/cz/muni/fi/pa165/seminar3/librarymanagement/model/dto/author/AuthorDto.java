package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author;


import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for an author. Data Transfer Object that is stable for API,
 * independent of internal Author class.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto extends DomainObjectDto {
    private String name;
    private String surname;
}
