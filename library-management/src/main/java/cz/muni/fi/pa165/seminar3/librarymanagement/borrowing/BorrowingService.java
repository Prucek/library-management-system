package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents borrowing service which enables working with JPA repository.
 *
 * @author Marek Miček
 */
@Service
public class BorrowingService extends DomainService<Borrowing> {

    @Getter
    private final BorrowingRepository repository;

    @Autowired
    public BorrowingService(BorrowingRepository repository) {
        this.repository = repository;
    }

    /**
     * Finds a pending borrowing.
     *
     * @param bookInstanceId id of the book instance
     * @return pending borrowing
     */
    public List<Borrowing> findPending(String bookInstanceId) {
        return repository.findPendingOfBookInstance(bookInstanceId);
    }
}
