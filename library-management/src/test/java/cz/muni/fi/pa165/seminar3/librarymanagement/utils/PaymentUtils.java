package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.FineUtils.fakeFine;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.FineUtils.fakeFineDto;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentStatus;
import cz.muni.fi.pa165.seminar3.librarymanagement.payment.Payment;
import java.util.ArrayList;
import java.util.List;

/**
 * Class containing payment test utility methods.
 *
 * @author Juraj Marcin
 */
public class PaymentUtils {

    /**
     * Generates a fake payment entity.
     *
     * @param faker faker instance
     * @return payment entity
     */

    public static Payment fakePayment(Faker faker) {
        return Payment.builder()
                .id(faker.internet().uuid())
                .transactionId(faker.internet().uuid())
                .status(PaymentStatus.PAID)
                .paidFines(new ArrayList<>(List.of(fakeFine(faker))))
                .build();
    }

    /**
     * Generates a fake payment dto.
     *
     * @param faker faker instance
     * @return payment dto
     */

    public static PaymentDto fakePaymentDto(Faker faker) {
        return PaymentDto.builder()
                .id(faker.internet().uuid())
                .transactionId(faker.internet().uuid())
                .status(PaymentStatus.PAID)
                .paidFines(new ArrayList<>(List.of(fakeFineDto(faker))))
                .build();
    }
}
