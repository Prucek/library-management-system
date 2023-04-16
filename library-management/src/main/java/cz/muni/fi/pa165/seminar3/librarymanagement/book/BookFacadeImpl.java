package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.AuthorService;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Class representing Book facade.
 *
 * @author Marek Fiala
 */
@Service
public class BookFacadeImpl extends DomainFacadeImpl<Book, BookDto, BookDto> implements BookFacade {
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
    public BookDto create(BookDto dto) {

        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book must have title.");
        }

        List<Author> authors = new ArrayList<>();
        for (AuthorDto authorDto : dto.getAuthors()) {
            Author x;
            try {
                x = authorService.find(authorDto.getId());
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException(e.toString());
            }
            authors.add(x);
        }
        dto.setAuthors(new ArrayList<>());

        Book newBook = domainMapper.fromDto(dto);
        newBook.setAuthors(authors);
        return domainMapper.toDto(domainService.create(newBook));
    }

    @Override
    public void delete(String id) {
        domainService.delete(domainService.find(id));
    }

    @Override
    public BookDto update(String id, BookDto dto) {
        Book book = domainService.find(id);
        book.setTitle(dto.getTitle());
        List<Author> newAuthors = new ArrayList<>();
        for (AuthorDto authorFromDto : dto.getAuthors()) {
            try {
                Author author = authorService.find(authorFromDto.getId());
                newAuthors.add(author);
            } catch (Exception ignored) {
                ;
            }
        }
        book.setAuthors(newAuthors);
        if (dto.getInstances() != null) {
            for (BookInstanceDto instFromDto : dto.getInstances()) {
                domainService.addInstance(book.getId(), instFromDto.getPages());
            }
        }
        return domainMapper.toDto(domainService.update(book));
    }

    @Override
    public BookInstanceDto addInstance(String bookId) {
        try {
            return instanceMapper.toDto(domainService.addInstance(bookId, 500));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
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
