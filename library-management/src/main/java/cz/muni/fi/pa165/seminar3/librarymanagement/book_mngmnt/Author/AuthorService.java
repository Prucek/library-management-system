package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Author;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService extends DomainService<Author> {

    @Getter
    private final AuthorRepository repository;

    @Autowired
    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Author find(String id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author " + id + "not found."));
    }
}
