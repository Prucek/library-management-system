package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
/**
 * DTO for a book. Data Transfer Object that is stable for API,
 * independent of internal Book class.
 */
@Getter
@Setter
public class BookDto extends DomainObjectDto {

    private String title;
    private List<AuthorDto> authors;
    private List<BookInstanceDto> instances;
}
