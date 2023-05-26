package cz.muni.fi.pa165.seminar3.paymentgate;

import static cz.muni.fi.pa165.seminar3.paymentgate.TransactionUtils.fakeCardDto;
import static cz.muni.fi.pa165.seminar3.paymentgate.TransactionUtils.fakeTransactionDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.CardDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import cz.muni.fi.pa165.seminar3.paymentgate.transaction.TransactionController;
import cz.muni.fi.pa165.seminar3.paymentgate.transaction.TransactionService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for TransactionController.
 *
 * @author Juraj Marcin
 */
@WebMvcTest(controllers = {TransactionController.class})
public class TransactionControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    private final Faker faker = new Faker();

    @Test
    public void createSuccessful() throws Exception {
        TransactionDto transactionDto = fakeTransactionDto(faker);
        // mock service
        given(transactionService.create(any(TransactionCreateDto.class))).willReturn(transactionDto);

        // perform test
        mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TransactionCreateDto.builder()
                                .amount(transactionDto.getAmount())
                                .callbackUrl(transactionDto.getCallbackUrl())
                                .build())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(transactionDto.getId()))
                .andExpect(jsonPath("$.amount").value(transactionDto.getAmount()))
                .andExpect(jsonPath("$.callbackUrl").value(transactionDto.getCallbackUrl()))
                .andExpect(jsonPath("$.status").value(transactionDto.getStatus().toString()));
    }

    @Test
    public void findAll() throws Exception {
        Result<TransactionDto> transactionDtoResult =
                Result.of(fakeTransactionDto(faker), fakeTransactionDto(faker), fakeTransactionDto(faker));
        // mock service
        given(transactionService.findAll(eq(0), anyInt())).willReturn(transactionDtoResult);

        // perform test
        mockMvc.perform(get("/transaction"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(transactionDtoResult.getTotal()))
                .andExpect(jsonPath("$.page").value(transactionDtoResult.getPage()))
                .andExpect(jsonPath("$.items[0].id").value(transactionDtoResult.getItems().get(0).getId()))
                .andExpect(jsonPath("$.items[1].id").value(transactionDtoResult.getItems().get(1).getId()))
                .andExpect(jsonPath("$.items[2].id").value(transactionDtoResult.getItems().get(2).getId()));
    }

    @Test
    public void findSuccessful() throws Exception {
        TransactionDto transactionDto = fakeTransactionDto(faker);
        // mock facade
        given(transactionService.find(eq(transactionDto.getId()))).willReturn(transactionDto);

        // perform test
        mockMvc.perform(get("/transaction/" + transactionDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transactionDto.getId()))
                .andExpect(jsonPath("$.amount").value(transactionDto.getAmount()))
                .andExpect(jsonPath("$.callbackUrl").value(transactionDto.getCallbackUrl()))
                .andExpect(jsonPath("$.status").value(transactionDto.getStatus().toString()));
    }

    @Test
    public void findNotFound() throws Exception {
        String transactionId = UUID.randomUUID().toString();
        // mock facade
        given(transactionService.find(eq(transactionId))).willThrow(NotFoundException.class);

        // perform test
        mockMvc.perform(get("/transaction/" + transactionId)).andExpect(status().isNotFound());
    }


    @Test
    public void paySuccessful() throws Exception {
        TransactionDto transactionDto = fakeTransactionDto(faker);
        CardDto cardDto = fakeCardDto(faker);
        // mock service
        given(transactionService.pay(eq(transactionDto.getId()), any(CardDto.class))).willReturn(transactionDto);

        // perform test
        mockMvc.perform(post("/transaction/" + transactionDto.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transactionDto.getId()))
                .andExpect(jsonPath("$.amount").value(transactionDto.getAmount()))
                .andExpect(jsonPath("$.callbackUrl").value(transactionDto.getCallbackUrl()))
                .andExpect(jsonPath("$.status").value(transactionDto.getStatus().toString()));
    }

    @Test
    public void payNotFound() throws Exception {
        String transactionId = UUID.randomUUID().toString();
        CardDto cardDto = fakeCardDto(faker);
        // mock service
        given(transactionService.pay(eq(transactionId), any(CardDto.class))).willThrow(NotFoundException.class);

        // perform test
        mockMvc.perform(post("/transaction/" + transactionId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDto))).andExpect(status().isNotFound());
    }
}
