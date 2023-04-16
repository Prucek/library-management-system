package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacade;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;

/**
 * Interface representing author facade.
 *
 * @author MarekFiala
 */
public interface AuthorFacade extends DomainFacade<AuthorDto, AuthorDto> {

    void delete(String id);

    AuthorDto update(String id, AuthorDto authorDto);

}
