package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
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
}
