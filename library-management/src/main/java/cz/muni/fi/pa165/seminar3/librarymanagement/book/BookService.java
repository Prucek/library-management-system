package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Book service providing access to Book repository
 */
@Service
public class BookService extends DomainService<Book> {

    @Getter
    private final BookRepository repository;

    private final BookInstanceRepository instanceRepository;

    @Autowired
    public BookService(BookRepository repository,
                       BookInstanceRepository instanceRepository) {
        this.repository = repository;
        this.instanceRepository = instanceRepository;
    }

    @Transactional(readOnly = true)
    public Book find(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book '" + id + "' not found."));
    }

    @Transactional
    public BookInstance addInstance(String bookId) {
        return instanceRepository.save(
                BookInstance.builder().id(UUID.randomUUID().toString()).book(find(bookId)).build());
    }

    @Transactional(readOnly = true)
    public BookInstance getInstance(String instanceId) {
        return instanceRepository.findById(instanceId)
                .orElseThrow(() -> new EntityNotFoundException("Book instance not found"));
    }

    @Transactional
    public void removeInstance(BookInstance bookInstance) {
        instanceRepository.delete(bookInstance);
    }
}

