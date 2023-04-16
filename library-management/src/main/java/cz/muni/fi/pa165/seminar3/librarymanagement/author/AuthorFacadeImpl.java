package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import cz.muni.fi.pa165.seminar3.librarymanagement.book.Book;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookService;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

    private final BookService bookService;

    /**
     * Creates a new author facade instance.
     *
     * @param domainService author service instance
     * @param domainMapper  author mapper instance
     * @param bookService   book service instance
     */
    public AuthorFacadeImpl(AuthorService domainService, AuthorMapper domainMapper, BookService bookService) {
        this.domainService = domainService;
        this.domainMapper = domainMapper;
        this.bookService = bookService;
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

        //        if(authorDto.getPublications() != null){
        //            List<Book> newPublications = null;
        //            for (BookDto bookFromDto : authorDto.getPublications()){
        //                try {
        //                    Book book = bookService.find(bookFromDto.getId());
        //                    newPublications.add(book);
        //                } catch (Exception ignored) {
        //                    ;
        //                }
        //            }
        //            author.setPublications(newPublications);
        //        }

        return domainMapper.toDto(domainService.update(author));
    }
}
