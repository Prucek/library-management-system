package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class representing payment service.
 *
 * @author Juraj Marcin
 */
@Service
public class PaymentService extends DomainService<Payment> {

    @Getter
    private final PaymentRepository repository;

    /**
     * Creates a new payment service instance.
     *
     * @param repository payment repository instance
     */
    @Autowired
    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    /**
     * Finds a payment by its transaction id.
     *
     * @param transactionId transaction id to find
     * @return found payment
     */
    public Payment findByTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId)
                .orElseThrow(() -> new NotFoundException(String.format("ID %s not found", transactionId)));
    }
}
