package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for a book. Data Transfer Object that is stable for API,
 * independent of internal Book class.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto extends DomainObjectDto {

    private String title;

    private List<AuthorDto> authors;

    private List<BookInstanceDto> instances;
}
