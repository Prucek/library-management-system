package cz.muni.fi.pa165.seminar3.paymentgate;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.CardDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentStatus;
import cz.muni.fi.pa165.seminar3.paymentgate.payment.PaymentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentGateApplicationTests {

    // injected mock implementation of Spring MVC
    @Autowired
    private MockMvc mockMvc;

    // injected mapper for converting classes into JSON strings
    @Autowired
    private ObjectMapper objectMapper;

    // injected mock implementation of the PaymentController
    @MockBean
    private PaymentController paymentController;

    @Test
    void contextLoads() {
    }

    @Test
    void createPayment() throws Exception {
        String id = "1";
        double amount = 125;
        PaymentStatus status = PaymentStatus.WAITING;
        String callbackUrl = "/payment/" + id;

        PaymentDto payment = new PaymentDto();
        payment.setAmount(amount); payment.setStatus(status);

        PaymentDto expectedPaymentDto = new PaymentDto();
        expectedPaymentDto.setId(id); expectedPaymentDto.setAmount(amount);
        expectedPaymentDto.setStatus(status); expectedPaymentDto.setCallbackURL("/payment/" + id);

        // define what mock service returns when called
        given(paymentController.create(payment)).willReturn(expectedPaymentDto);

        // call controller and check the result
        mockMvc.perform(post("/payment")
                    .header("User-Agent", "007")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(payment))

                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.callbackURL").value(callbackUrl))
                .andDo(print());
    }

    @Test
    void getNonExistingPayment() throws Exception {
        // define what mock service returns when called
       given(paymentController.find("-1")).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
               "payment with id=-1 not found"));

        // call controller and check the result
        mockMvc.perform(get("/payment/-1")
                        .header("User-Agent", "007")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andDo(print());
    }

    @Test
    void getExistingPayment() throws Exception {
        String id = "1";
        double amount = 125;
        PaymentStatus status = PaymentStatus.WAITING;
        String callbackUrl = "/payment/" + id;

        PaymentDto payment = new PaymentDto();
        payment.setId(id); payment.setCallbackURL(callbackUrl);
        payment.setAmount(amount); payment.setStatus(status);

        given(paymentController.find(id)).willReturn(payment);
        // call controller and check the result
        mockMvc.perform(get("/payment/" + id)
                        .header("User-Agent", "007")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(amount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(status.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.callbackURL").value(callbackUrl))
                .andDo(print());
    }

    @Test
    void payForNonExistingPayment() throws Exception {
        // define what mock service returns when called
        given(paymentController.pay("-1", null)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                "payment with id=-1 not found"));

        // call controller and check the result
        mockMvc.perform(post("/payment/-1")
                        .header("User-Agent", "007")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andDo(print());
    }

    @Test
    void payWithNonExistingCard() throws Exception {
        CardDto card = new CardDto();

        given(paymentController.pay("1", card)).willReturn(new PaymentDto());

        // call controller and check the result
        mockMvc.perform(post("/payment/1")
                        .header("User-Agent", "007")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(card)))
                .andExpect(status().isOk());
    }

    @Test
    void payWithExistingCard() throws Exception {
        CardDto card = new CardDto();
        card.setId("1"); card.setCardNumber("0458 0245 1547 1528");
        card.setExpiration("23/10/2023"); card.setCvv2("147");

        given(paymentController.pay("1", card)).willReturn(new PaymentDto());

        // call controller and check the result
        mockMvc.perform(post("/payment/1")
                        .header("User-Agent", "007")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(card)))
                .andExpect(status().isOk());
    }
}
