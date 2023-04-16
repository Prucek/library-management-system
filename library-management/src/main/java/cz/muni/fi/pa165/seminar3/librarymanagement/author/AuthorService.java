package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author service providing access to Author repository.
 *
 * @author Marek Fiala
 */
@Service
public class AuthorService extends DomainService<Author> {

    @Getter
    private final AuthorRepository repository;

    /**
     * Creates a new author service instance.
     *
     * @param repository author repository instance
     */
    @Autowired
    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }
}
