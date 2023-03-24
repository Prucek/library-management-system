package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Book;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Book service providing access to Book repository
 */
@Service
public class BookService extends DomainService<Book> {

    @Getter
    private final BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Book find(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book '" + id + "' not found."));
    }

}

