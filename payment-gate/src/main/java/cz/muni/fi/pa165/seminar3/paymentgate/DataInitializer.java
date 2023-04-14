package cz.muni.fi.pa165.seminar3.paymentgate;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionStatus;
import cz.muni.fi.pa165.seminar3.paymentgate.transaction.Transaction;
import cz.muni.fi.pa165.seminar3.paymentgate.transaction.TransactionService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Data initializer for the payment gate.
 *
 * @author Peter Rúček
 */
@Component
public class DataInitializer implements ApplicationRunner {

    private final TransactionService transactionService;

    public DataInitializer(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void run(ApplicationArguments args) {
        Transaction transaction = Transaction.builder()
                .status(TransactionStatus.APPROVED)
                .amount(100)
                .build();
        transactionService.create(transaction);

        Transaction transaction2 = Transaction.builder()
                .amount(45.45)
                .build();
        transactionService.create(transaction2);
    }
}
