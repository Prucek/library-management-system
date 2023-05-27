package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.PaymentUtils.fakePayment;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests for the borrowing repository.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LibraryManagementApplication.class)
@Transactional
public class PaymentRepositoryTests {
    @Autowired
    private EntityManager em;

    @Autowired
    private PaymentRepository paymentRepository;

    private Payment payment;

    private final Faker faker = new Faker();

    /**
     * Setups entities for tests.
     */
    @BeforeEach
    public void beforeEach() {
        Payment fakePayment = fakePayment(faker);
        fakePayment.setPaidFines(Collections.emptyList());
        payment = em.merge(fakePayment);
    }

    @Test
    public void findExisting() {
        Optional<Payment> result = paymentRepository.findByTransactionId(payment.getTransactionId());

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(payment.getId());
    }

    @Test
    public void findNonExisting() {
        Optional<Payment> result = paymentRepository.findByTransactionId("-1");

        assertThat(result.isPresent()).isFalse();
    }

    /**
     * Clears the database.
     */
    @AfterEach
    public void afterEach() {
        em.remove(payment);
    }
}
