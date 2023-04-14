package cz.muni.fi.pa165.seminar3.paymentgate;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.CardDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionStatus;
import cz.muni.fi.pa165.seminar3.paymentgate.transaction.TransactionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class PaymentGateApplicationTests {

    // injected mock implementation of Spring MVC
    @Autowired
    private MockMvc mockMvc;

    // injected mapper for converting classes into JSON strings
    @Autowired
    private ObjectMapper objectMapper;

    // injected mock implementation of the PaymentController
    @MockBean
    private TransactionController transactionController;

    @Test
    void contextLoads() {
    }

    @Test
    void createTransaction() throws Exception {

        String id = "1";
        double amount = 125;
        String callbackUrl = "/transactions/" + id;

        TransactionCreateDto transactionDto = TransactionCreateDto.builder().amount(amount).build();

        TransactionDto expectedTransactionDto = TransactionDto.builder()
                .id(id)
                .amount(amount)
                .callbackUrl(callbackUrl)
                .status(TransactionStatus.WAITING)
                .build();

        // define what mock service returns when called
        given(transactionController.create(any(TransactionCreateDto.class))).willReturn(expectedTransactionDto);

        // call controller and check the result
        mockMvc.perform(post("/transactions").header("User-Agent", "007")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(TransactionStatus.WAITING.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.callbackUrl").value(callbackUrl))
                .andDo(print());
    }

    @Test
    void getNonExistingTransaction() throws
            Exception {
        // define what mock service returns when called
        given(transactionController.find("-1")).willThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "transaction with id=-1 not found"));

        // call controller and check the result
        mockMvc.perform(get("/transactions/-1").header("User-Agent", "007").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getExistingTransaction() throws
            Exception {
        String id = "1";
        double amount = 125;
        TransactionStatus status = TransactionStatus.WAITING;
        String callbackUrl = "/transactions/" + id;

        TransactionDto transactionDto =
                TransactionDto.builder().id(id).callbackUrl(callbackUrl).amount(amount).status(status).build();

        given(transactionController.find(id)).willReturn(transactionDto);
        // call controller and check the result
        mockMvc.perform(get("/transactions/" + id).header("User-Agent", "007").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.callbackUrl").value(callbackUrl))
                .andDo(print());
    }

    @Test
    void payForNonExistingTransaction() throws
            Exception {
        // define what mock service returns when called
        given(transactionController.pay("-1", null)).willThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "transaction with id=-1 not found"));

        // call controller and check the result
        mockMvc.perform(post("/transactions/-1").header("User-Agent", "007").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andDo(print());
    }

    @Test
    void payWithNonExistingCard() throws
            Exception {
        CardDto card = CardDto.builder().build();

        given(transactionController.pay("1", card)).willReturn(TransactionDto.builder().build());

        // call controller and check the result
        mockMvc.perform(post("/transactions/1").header("User-Agent", "007")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(card))).andExpect(status().isOk());
    }

    @Test
    void payWithExistingCard() throws
            Exception {
        CardDto card = CardDto.builder().build();
        card.setId("1");
        card.setCardNumber("0458 0245 1547 1528");
        card.setExpiration("23/10/2023");
        card.setCvv2("147");

        given(transactionController.pay("1", card)).willReturn(TransactionDto.builder().build());

        // call controller and check the result
        mockMvc.perform(post("/transactions/1").header("User-Agent", "007")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(card))).andExpect(status().isOk());
    }
}
