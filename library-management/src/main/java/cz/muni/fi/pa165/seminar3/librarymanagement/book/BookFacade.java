package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacade;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;

/**
 * Interface representing book facade.
 *
 * @author MarekFiala
 */
public interface BookFacade extends DomainFacade<BookDto, BookCreateDto> {

    /**
     * Deletes a book.
     *
     * @param id id of the book
     */
    void delete(String id);

    /**
     * Updates a book.
     *
     * @param id  id of the book
     * @param dto updated book date
     * @return updated book
     */
    BookDto update(String id, BookCreateDto dto);

    /**
     * Adds an instance to a book.
     *
     * @param bookId id of the book
     * @return new book instance
     */
    BookInstanceDto addInstance(String bookId);

    /**
     * Removes a book instance.
     *
     * @param id id of the book instance
     */
    void removeInstance(String id);

    /**
     * Finds a book instance.
     *
     * @param instanceId id of the book instance
     * @return found instance
     */
    BookInstanceDto getInstance(String instanceId);
}
