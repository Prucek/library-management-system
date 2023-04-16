package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacade;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;

/**
 * Interface representing book facade.
 *
 * @author MarekFiala
 */
public interface BookFacade extends DomainFacade<BookDto, BookDto> {

    void delete(String id);

    BookDto update(String id, BookDto dto);

    BookInstanceDto addInstance(String bookId);

    void removeInstance(String id);

    BookInstanceDto getInstance(String instanceId);
}
