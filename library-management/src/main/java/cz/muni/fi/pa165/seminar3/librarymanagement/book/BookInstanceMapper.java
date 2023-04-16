package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import org.mapstruct.Mapper;

/**
 * Mapper mapping Book DTO to Book.
 *
 * @author MarekFiala
 */
@Mapper
public interface BookInstanceMapper extends DomainMapper<BookInstance, BookInstanceDto> {

}
