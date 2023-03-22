package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for a book. Data Transfer Object that is stable for API,
 * independent of internal User class.
 */
@Getter
@Setter
public class BookDto extends DomainObjectDto {

    private String title;
    private String author;
}
