package cz.muni.fi.pa165.seminar3.paymentgate.transaction;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Class representing transaction service.
 */
@Service
public class TransactionService {

    @Getter
    private final TransactionRepository repository;

    @Autowired
    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new Transaction entity.
     *
     * @param entity Transaction to create
     * @return new Transaction
     */
    public Transaction create(Transaction entity) {
        entity.setStatus(TransactionStatus.WAITING);
        Transaction transaction = repository.save(entity);
        transaction.setCallbackUrl("/transactions/" + transaction.getId());
        return repository.save(transaction);
    }

    /**
     * Updates a Transaction entity.
     *
     * @param entity Transaction entity to update
     * @return updated Transaction entity
     */
    public Transaction update(Transaction entity) {
        return repository.save(entity);
    }

    /**
     * Finds a Transaction entity with id.
     *
     * @param id id to search for
     * @return found Transaction entity
     * @throws EntityNotFoundException if the id was not found
     */
    public Transaction find(String id) throws EntityNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("ID %s not found", id)));
    }

    /**
     * Finds all Transactions.
     *
     * @param pageable pagination information
     * @return page of found Transactions
     */
    public Page<Transaction> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
