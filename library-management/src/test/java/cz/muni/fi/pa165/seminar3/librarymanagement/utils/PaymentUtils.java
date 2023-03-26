package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentStatus;
import cz.muni.fi.pa165.seminar3.librarymanagement.payment.Payment;

import java.util.List;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.FineUtils.fakeFine;

public class PaymentUtils {
    public static Payment fakePayment() {
        return fakePayment(new Faker());
    }

    public static Payment fakePayment(Faker faker) {
        return Payment.builder()
                .id(faker.internet().uuid())
                .transactionId(faker.internet().uuid())
                .status(PaymentStatus.PAID)
                .paidFines(List.of(fakeFine(faker)))
                .build();
    }
}
