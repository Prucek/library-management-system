package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Author;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Author.AuthorCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Author.AuthorDto;
import org.mapstruct.Mapper;

@Mapper
public interface AuthorMapper extends DomainMapper<Author, AuthorDto> {
    Author fromCreateDto(AuthorCreateDto dto);
}
