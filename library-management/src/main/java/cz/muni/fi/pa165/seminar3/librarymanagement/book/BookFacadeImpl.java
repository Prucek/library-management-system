package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.author.AuthorService;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import lombok.Getter;
import org.springframework.stereotype.Service;

/**
 * Class representing Book facade.
 *
 * @author Marek Fiala
 */
@Service
public class BookFacadeImpl extends DomainFacadeImpl<Book, BookDto, BookCreateDto> implements BookFacade {
    @Getter
    private final BookService domainService;
    private final AuthorService authorService;
    @Getter
    private final BookMapper domainMapper;
    private final BookInstanceMapper instanceMapper;

    /**
     * Creates a new book facade instance.
     *
     * @param service        book service instance
     * @param authorService  author service instance
     * @param domainMapper   domain mapper instance
     * @param instanceMapper book-instance mapper instance
     */
    public BookFacadeImpl(BookService service, AuthorService authorService, BookMapper domainMapper,
                          BookInstanceMapper instanceMapper) {
        this.domainService = service;
        this.authorService = authorService;
        this.domainMapper = domainMapper;
        this.instanceMapper = instanceMapper;
    }

    @Override
    public BookDto create(BookCreateDto createDto) {
        Book book = Book.builder()
                .title(createDto.getTitle())
                .authors(createDto.getAuthorIds().stream().map(authorService::find).toList())
                .build();
        return domainMapper.toDto(domainService.create(book));
    }

    @Override
    public void delete(String id) {
        domainService.delete(domainService.find(id));
    }

    @Override
    public BookDto update(String id, BookCreateDto dto) {
        Book book = domainService.find(id);
        book.setTitle(dto.getTitle());
        book.getAuthors().removeIf(author -> !dto.getAuthorIds().contains(author.getId()));
        book.getAuthors()
                .addAll(dto.getAuthorIds()
                        .stream()
                        .filter(authorId -> book.getAuthors()
                                .stream()
                                .noneMatch(author -> author.getId().equals(authorId)))
                        .map(authorService::find)
                        .toList());
        return domainMapper.toDto(domainService.update(book));
    }

    @Override
    public BookInstanceDto addInstance(String bookId) {
        return instanceMapper.toDto(domainService.addInstance(bookId));
    }

    @Override
    public void removeInstance(String id) {
        domainService.removeInstance(domainService.getInstance(id));
    }

    @Override
    public BookInstanceDto getInstance(String instanceId) {
        return instanceMapper.toDto(domainService.getInstance(instanceId));
    }
}
