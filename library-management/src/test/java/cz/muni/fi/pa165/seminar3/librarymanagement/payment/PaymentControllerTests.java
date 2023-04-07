package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.FineService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentStatus;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.PaymentUtils.fakePayment;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PaymentController.class, PaymentMapper.class})
public class PaymentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private FineService fineService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Test
    public void createSuccessful() throws
            Exception {
        Payment payment = fakePayment(faker);
        // mock services
        given(fineService.find(payment.getPaidFines().get(0).getId())).willReturn(payment.getPaidFines().get(0));
        given(paymentService.create(any())).willReturn(payment);

        // perform test
        mockMvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                PaymentCreateDto.builder().fines(List.of(payment.getPaidFines().get(0).getId())).build())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(payment.getId()))
                .andExpect(jsonPath("$.transactionId").value(payment.getTransactionId()))
                .andExpect(jsonPath("$.status").value(payment.getStatus().toString()))
                .andExpect(jsonPath("$.paidFines[0].id").value(payment.getPaidFines().get(0).getId()));
    }

    @Test
    public void createNotFound() throws
            Exception {
        // mock services
        given(fineService.find(any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                PaymentCreateDto.builder().fines(List.of(UUID.randomUUID().toString())).build())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void paymentGateCallbackSuccessful() throws
            Exception {
        Payment payment = fakePayment(faker);
        payment.setStatus(PaymentStatus.CREATED);
        // mock services
        given(paymentService.find(payment.getId())).willReturn(payment);
        given(paymentService.update(payment)).willReturn(payment);

        // perform test
        mockMvc.perform(post("/payments/" + payment.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(payment.getId()))
                .andExpect(jsonPath("$.transactionId").value(payment.getTransactionId()))
                .andExpect(jsonPath("$.status").value(payment.getStatus().toString()))
                .andExpect(jsonPath("$.paidFines[0].id").value(payment.getPaidFines().get(0).getId()));
    }

    @Test
    public void paymentGateCallbackNotFound() throws
            Exception {
        // mock services
        given(paymentService.find(any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(post("/payments/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    public void findAll() throws
            Exception {
        List<Payment> payments = List.of(fakePayment(faker), fakePayment(faker), fakePayment(faker));
        // mock services
        given(paymentService.findAll(any())).willReturn(new PageImpl<>(payments));

        // perform test
        mockMvc.perform(get("/payments"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.total").value(payments.size()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.items[0].id").value(payments.get(0).getId()))
                .andExpect(jsonPath("$.items[1].id").value(payments.get(1).getId()))
                .andExpect(jsonPath("$.items[2].id").value(payments.get(2).getId()));
    }

    @Test
    public void findSuccessful() throws
            Exception {
        Payment payment = fakePayment(faker);
        // mock services
        given(paymentService.find(payment.getId())).willReturn(payment);

        // perform test
        mockMvc.perform(get("/payments/" + payment.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(payment.getId()))
                .andExpect(jsonPath("$.transactionId").value(payment.getTransactionId()))
                .andExpect(jsonPath("$.status").value(payment.getStatus().toString()))
                .andExpect(jsonPath("$.paidFines[0].id").value(payment.getPaidFines().get(0).getId()));
    }

    @Test
    public void findNotFound() throws
            Exception {
        // mock services
        given(paymentService.find(any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(get("/payments/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }
}
