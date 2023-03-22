package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents borrowing service which enables working with JPA repository
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

    @Transactional(readOnly = true)
    public Borrowing find(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Borrowing with id '" + id + "' not found."));
    }

}
