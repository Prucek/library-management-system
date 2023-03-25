package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Book;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book.BookDto;
import org.mapstruct.Mapper;

/**
 * Mapper mapping Book DTO to Book
 */
@Mapper
public interface BookMapper extends DomainMapper<Book, BookDto> {
}
