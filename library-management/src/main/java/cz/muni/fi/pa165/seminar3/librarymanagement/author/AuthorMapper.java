package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import org.mapstruct.Mapper;

/**
 * Mapper mapping Author DTO to Author.
 *
 * @author Marek Fiala
 */
@Mapper
public interface AuthorMapper extends DomainMapper<Author, AuthorDto> {
}
