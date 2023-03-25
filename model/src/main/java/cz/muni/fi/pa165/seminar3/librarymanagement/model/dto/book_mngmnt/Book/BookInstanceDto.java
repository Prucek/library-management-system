package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for an individual Book instance. Data Transfer Object that is stable for API,
 * independent of internal BookInstance class.
 */
@Getter
@Setter
public class BookInstanceDto extends DomainObjectDto {
    String ISBN;
    //    Note: Asi nepotrebne jelikoz instance jsou navazany primo v jednotlivych knihach
//    private BookDto bookReference;
}