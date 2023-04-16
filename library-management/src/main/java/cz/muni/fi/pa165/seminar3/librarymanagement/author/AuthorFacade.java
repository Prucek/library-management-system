package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacade;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;

/**
 * Interface representing author facade.
 *
 * @author MarekFiala
 */
public interface AuthorFacade extends DomainFacade<AuthorDto, AuthorCreateDto> {

    void delete(String id);

    AuthorDto update(String id, AuthorCreateDto authorDto);
}
