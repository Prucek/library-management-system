package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Book service providing access to Book repository.
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

    /**
     * Add new book instance to instanceRepository.
     *
     * @param bookId  Book id instance is associated with
     * @param pages   Number of instance pages
     * @return new book instance
     */
    @Transactional
    public BookInstance addInstance(String bookId, Integer pages) {
        return instanceRepository.save(
                BookInstance.builder().id(UUID.randomUUID().toString())
                        .bookAssigned(find(bookId)).pages(pages).build());
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

