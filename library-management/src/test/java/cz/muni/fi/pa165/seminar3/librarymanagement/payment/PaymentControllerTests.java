package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.USER_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.PaymentUtils.fakePaymentDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for payment controller.
 */
@WebMvcTest(controllers = {PaymentController.class, PaymentMapper.class})
public class PaymentControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentFacade paymentFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void createSuccessful() throws Exception {
        PaymentDto paymentDto = fakePaymentDto(faker);
        // mock facade
        given(paymentFacade.create(any())).willReturn(paymentDto);

        // perform test
        mockMvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON).with(csrf())
                        .content(objectMapper.writeValueAsString(PaymentCreateDto.builder()
                                .fines(paymentDto.getPaidFines().stream().map(DomainObjectDto::getId).toList())
                                .build())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(paymentDto.getId()))
                .andExpect(jsonPath("$.transactionId").value(paymentDto.getTransactionId()))
                .andExpect(jsonPath("$.status").value(paymentDto.getStatus().toString()))
                .andExpect(jsonPath("$.paidFines[0].id").value(paymentDto.getPaidFines().get(0).getId()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void createNotFound() throws Exception {
        // mock facade
        given(paymentFacade.create(any())).willThrow(NotFoundException.class);

        // perform test
        mockMvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON).with(csrf())
                        .content(objectMapper.writeValueAsString(
                                PaymentCreateDto.builder().fines(List.of(UUID.randomUUID().toString())).build())))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void paymentGateCallbackSuccessful() throws Exception {
        PaymentDto paymentDto = fakePaymentDto(faker);
        // mock facade
        given(paymentFacade.finalizePayment(eq(paymentDto.getTransactionId()))).willReturn(paymentDto);

        // perform test
        mockMvc.perform(post("/payments/" + paymentDto.getTransactionId()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentDto.getId()))
                .andExpect(jsonPath("$.transactionId").value(paymentDto.getTransactionId()))
                .andExpect(jsonPath("$.status").value(paymentDto.getStatus().toString()))
                .andExpect(jsonPath("$.paidFines[0].id").value(paymentDto.getPaidFines().get(0).getId()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void paymentGateCallbackNotFound() throws Exception {
        // mock facade
        given(paymentFacade.finalizePayment(any())).willThrow(NotFoundException.class);

        // perform test
        mockMvc.perform(post("/payments/" + UUID.randomUUID()).with(csrf())).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    public void findAll() throws Exception {
        Result<PaymentDto> paymentDtoResult =
                Result.of(fakePaymentDto(faker), fakePaymentDto(faker), fakePaymentDto(faker));
        // mock facade
        given(paymentFacade.findAll(eq(0), anyInt())).willReturn(paymentDtoResult);

        // perform test
        mockMvc.perform(get("/payments").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(paymentDtoResult.getTotal()))
                .andExpect(jsonPath("$.page").value(paymentDtoResult.getPage()))
                .andExpect(jsonPath("$.items[0].id").value(paymentDtoResult.getItems().get(0).getId()))
                .andExpect(jsonPath("$.items[1].id").value(paymentDtoResult.getItems().get(1).getId()))
                .andExpect(jsonPath("$.items[2].id").value(paymentDtoResult.getItems().get(2).getId()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void findSuccessful() throws Exception {
        PaymentDto paymentDto = fakePaymentDto(faker);
        // mock facade
        given(paymentFacade.find(eq(paymentDto.getId()))).willReturn(paymentDto);

        // perform test
        mockMvc.perform(get("/payments/" + paymentDto.getId()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentDto.getId()))
                .andExpect(jsonPath("$.transactionId").value(paymentDto.getTransactionId()))
                .andExpect(jsonPath("$.status").value(paymentDto.getStatus().toString()))
                .andExpect(jsonPath("$.paidFines[0].id").value(paymentDto.getPaidFines().get(0).getId()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void findNotFound() throws Exception {
        // mock facade
        given(paymentFacade.find(any())).willThrow(NotFoundException.class);

        // perform test
        mockMvc.perform(get("/payments/" + UUID.randomUUID()).with(csrf())).andExpect(status().isNotFound());
    }
}
