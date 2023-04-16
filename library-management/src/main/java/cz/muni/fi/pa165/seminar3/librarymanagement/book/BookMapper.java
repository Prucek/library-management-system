package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper mapping Book DTO to Book.
 *
 * @author MarekFiala
 */
//@Mapper(uses = AuthorMapper.class)
@Mapper
public interface BookMapper extends DomainMapper<Book, BookDto> {
//    @Override
//    @Mapping(target = "instances.bookAssigned", ignore = true)
//    BookDto toDto(Book book);
}
