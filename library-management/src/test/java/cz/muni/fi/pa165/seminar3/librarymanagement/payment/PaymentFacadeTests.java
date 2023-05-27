package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.FineUtils.fakeFine;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.PaymentUtils.fakePayment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.Fine;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.FineService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentStatus;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionStatus;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Tests for Payment facade.
 *
 * @author Peter Rúček
 */
@WebMvcTest(controllers = {PaymentFacade.class, PaymentMapper.class})
public class PaymentFacadeTests {

    @MockBean
    private PaymentService domainService;

    @MockBean
    private FineService fineService;

    @MockBean
    private PaymentGateApi paymentGateApi;

    @Autowired
    private PaymentFacadeImpl paymentFacade;


    private final Faker faker = new Faker();

    @Test
    public void createPaymentSuccessful() {
        List<String> fines = List.of("randomId", "randomId2");
        PaymentCreateDto paymentCreateDto = PaymentCreateDto.builder().fines(fines).build();

        TransactionDto transactionDto = TransactionDto.builder().id("randomId").build();

        Fine fakeFine1 = fakeFine(faker);
        Fine fakeFine2 = fakeFine(faker);
        List<Fine> fakeFines = List.of(fakeFine1, fakeFine2);

        Payment payment = Payment.builder()
                .status(PaymentStatus.CREATED)
                .paidFines(fakeFines)
                .transactionId(transactionDto.getId())
                .build();

        given(fineService.find(eq("randomId"))).willReturn(fakeFine1);
        given(fineService.find(eq("randomId2"))).willReturn(fakeFine2);
        given(paymentGateApi.createTransaction(any(Double.class), any(String.class))).willReturn(transactionDto);
        given(domainService.create(any(Payment.class))).willReturn(payment);

        PaymentDto result = paymentFacade.create(paymentCreateDto);
        assertThat(result.getId()).isEqualTo(payment.getId());
        assertThat(result.getTransactionId()).isEqualTo(payment.getTransactionId());
    }

    @Test
    public void createPaymentEmptyFines() {

        List<String> fines = List.of();
        PaymentCreateDto paymentCreateDto = PaymentCreateDto.builder().fines(fines).build();

        TransactionDto transactionDto = TransactionDto.builder().id("randomId").build();

        List<Fine> fakeFines = List.of();

        Payment payment = Payment.builder()
                .status(PaymentStatus.CREATED)
                .paidFines(fakeFines)
                .transactionId(transactionDto.getId())
                .build();

        given(fineService.find(any())).willCallRealMethod();
        given(paymentGateApi.createTransaction(any(Double.class), any(String.class))).willReturn(transactionDto);
        given(domainService.create(any(Payment.class))).willReturn(payment);

        PaymentDto result = paymentFacade.create(paymentCreateDto);
        assertThat(result.getId()).isEqualTo(payment.getId());
        assertThat(result.getTransactionId()).isEqualTo(payment.getTransactionId());
    }

    @Test
    public void createPaymentNullFines() {

        PaymentCreateDto paymentCreateDto = PaymentCreateDto.builder().build();

        assertThrows(NullPointerException.class, () -> paymentFacade.create(paymentCreateDto));
    }

    @Test
    public void finalizePaymentCreated() {
        Payment payment = fakePayment(faker);

        TransactionDto transactionDto = TransactionDto.builder().status(TransactionStatus.WAITING).build();

        given(domainService.findByTransactionId(payment.getTransactionId())).willReturn(payment);
        given(paymentGateApi.findTransaction(payment.getTransactionId())).willReturn(transactionDto);

        PaymentDto result = paymentFacade.finalizePayment(payment.getTransactionId());
        assertThat(result.getId()).isEqualTo(payment.getId());
        assertThat(result.getTransactionId()).isEqualTo(payment.getTransactionId());
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.CREATED);
    }

    @Test
    public void finalizePaymentPaid() {
        Payment payment = fakePayment(faker);

        TransactionDto transactionDto = TransactionDto.builder().status(TransactionStatus.APPROVED).build();

        given(domainService.findByTransactionId(payment.getTransactionId())).willReturn(payment);
        given(paymentGateApi.findTransaction(payment.getTransactionId())).willReturn(transactionDto);

        PaymentDto result = paymentFacade.finalizePayment(payment.getTransactionId());
        assertThat(result.getId()).isEqualTo(payment.getId());
        assertThat(result.getTransactionId()).isEqualTo(payment.getTransactionId());
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.PAID);
    }

    @Test
    public void finalizePaymentCanceled() {
        Payment payment = fakePayment(faker);

        TransactionDto transactionDto = TransactionDto.builder().status(TransactionStatus.DECLINED).build();

        given(domainService.findByTransactionId(payment.getTransactionId())).willReturn(payment);
        given(paymentGateApi.findTransaction(payment.getTransactionId())).willReturn(transactionDto);

        PaymentDto result = paymentFacade.finalizePayment(payment.getTransactionId());
        assertThat(result.getId()).isEqualTo(payment.getId());
        assertThat(result.getTransactionId()).isEqualTo(payment.getTransactionId());
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.CANCELED);
    }
}
