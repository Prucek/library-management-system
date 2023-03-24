package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Book;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book.BookCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book.BookDto;
import org.mapstruct.Mapper;

/**
 * Mapper mapping Book DTOs to Book
 */
@Mapper
public interface BookMapper extends DomainMapper<Book, BookDto> {

    Book fromCreateDto(BookCreateDto dto);
}
