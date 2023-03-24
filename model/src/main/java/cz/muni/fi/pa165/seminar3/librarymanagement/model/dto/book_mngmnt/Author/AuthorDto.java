package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Author;


import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for an author. Data Transfer Object that is stable for API,
 * independent of internal Author class.
 */
@Getter
@Setter
public class AuthorDto extends DomainObjectDto {
    private String name;
    private String surname;
}
