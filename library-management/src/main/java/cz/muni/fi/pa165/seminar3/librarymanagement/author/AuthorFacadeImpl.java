package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import cz.muni.fi.pa165.seminar3.librarymanagement.book.Book;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


/**
 * Class representing Author facade.
 *
 * @author MarekFiala
 */
@Service
public class AuthorFacadeImpl extends DomainFacadeImpl<Author, AuthorDto, AuthorDto> implements AuthorFacade {
    @Getter
    private final AuthorService domainService;

    @Getter
    private final AuthorMapper domainMapper;

    /**
     * Creates a new author facade instance.
     *
     * @param domainService author service instance
     * @param domainMapper  author mapper instance
     */
    public AuthorFacadeImpl(AuthorService domainService, AuthorMapper domainMapper) {
        this.domainService = domainService;
        this.domainMapper = domainMapper;
    }

    @Override
    public AuthorDto create(AuthorDto createDto) {
        if (createDto.getName() == null || createDto.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author must have name.");
        }
        if (createDto.getSurname() == null || createDto.getSurname().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author must have surname.");
        }
        return domainMapper.toDto(domainService.create(domainMapper.fromDto(createDto)));
    }

    @Override
    public void delete(String id) {
        Author a = domainService.find(id);
        for (Book book : a.getPublications()) {
            book.getAuthors().remove(a);
        }
        domainService.delete(domainService.find(id));
    }

    @Override
    public AuthorDto update(String id, AuthorDto authorDto) {
        Author author = domainService.find(id);
        if (authorDto.getName() != null) {
            author.setName(authorDto.getName());
        }
        if (authorDto.getSurname() != null) {
            author.setSurname(authorDto.getSurname());
        }

        return domainMapper.toDto(domainService.update(author));
    }
}
