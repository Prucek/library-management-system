package cz.muni.fi.pa165.seminar3.paymentgate;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.CardDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionStatus;
import cz.muni.fi.pa165.seminar3.paymentgate.transaction.Transaction;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * Class containing transaction test utility methods.
 *
 * @author Juraj Marcin
 */
public class TransactionUtils {

    /**
     * Generates a fake transaction dto.
     *
     * @param faker faker instance
     * @return fake transaction
     */
    public static TransactionDto fakeTransactionDto(Faker faker) {
        return TransactionDto.builder()
                .id(faker.internet().uuid())
                .amount(faker.number().randomDouble(2, 1, 100))
                .status(TransactionStatus.APPROVED)
                .callbackUrl(faker.internet().url())
                .build();
    }

    /**
     * Generates a fake transaction entity.
     *
     * @param faker faker instance
     * @return fake transaction
     */
    public static Transaction fakeTransaction(Faker faker) {
        return Transaction.builder()
                .id(faker.internet().uuid())
                .amount(faker.number().randomDouble(2, 1, 100))
                .status(TransactionStatus.APPROVED)
                .callbackUrl(faker.internet().url())
                .build();
    }

    /**
     * Generates a fake card.
     *
     * @param faker faker instance
     * @return fake card
     */
    public static CardDto fakeCardDto(Faker faker) {
        return CardDto.builder()
                .cardNumber(faker.finance().creditCard())
                .cvv2(Integer.toString(faker.number().numberBetween(100, 999)))
                .expiration(new SimpleDateFormat("MM/yy").format(faker.date().future(365, TimeUnit.DAYS)))
                .build();
    }
}
