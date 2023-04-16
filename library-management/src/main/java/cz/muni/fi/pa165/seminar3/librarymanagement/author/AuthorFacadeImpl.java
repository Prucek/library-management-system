package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import lombok.Getter;
import org.springframework.stereotype.Service;

/**
 * Class representing Author facade.
 *
 * @author MarekFiala
 */
@Service
public class AuthorFacadeImpl extends DomainFacadeImpl<Author, AuthorDto, AuthorCreateDto> implements AuthorFacade {
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
    public AuthorDto create(AuthorCreateDto createDto) {
        Author author = Author.builder().name(createDto.getName()).surname(createDto.getSurname()).build();
        return domainMapper.toDto(domainService.create(author));
    }

    @Override
    public void delete(String id) {
        Author author = domainService.find(id);
        domainService.delete(author);
    }

    @Override
    public AuthorDto update(String id, AuthorCreateDto authorDto) {
        Author author = domainService.find(id);
        author.setName(authorDto.getName());
        author.setSurname(authorDto.getSurname());

        return domainMapper.toDto(domainService.update(author));
    }
}
