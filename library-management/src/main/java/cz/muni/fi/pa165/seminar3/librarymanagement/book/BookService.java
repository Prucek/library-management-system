package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Book service providing access to Book repository.
 */
@Service
public class BookService extends DomainService<Book> {

    @Getter
    private final BookRepository repository;

    private final BookInstanceRepository instanceRepository;

    @Autowired
    public BookService(BookRepository repository, BookInstanceRepository instanceRepository) {
        this.repository = repository;
        this.instanceRepository = instanceRepository;
    }

    /**
     * Add new book instance to instanceRepository.
     *
     * @param bookId Book id instance is associated with
     * @return new book instance
     */
    public BookInstance addInstance(String bookId) {
        return instanceRepository.save(BookInstance.builder().book(find(bookId)).build());
    }

    public BookInstance getInstance(String instanceId) {
        return instanceRepository.findById(instanceId)
                .orElseThrow(() -> new NotFoundException("Book instance not found"));
    }

    public void removeInstance(BookInstance bookInstance) {
        instanceRepository.delete(bookInstance);
    }
}

