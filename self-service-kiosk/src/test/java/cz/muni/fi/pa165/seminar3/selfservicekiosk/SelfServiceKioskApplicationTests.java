package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import static cz.muni.fi.pa165.seminar3.selfservicekiosk.KioskUtils.fakeKioskBorrowingDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for self-service kiosk controller.
 */
@WebMvcTest(KioskController.class)
class SelfServiceKioskApplicationTests {

    // injected mock implementation of Spring MVC
    @Autowired
    private MockMvc mockMvc;

    // injected mapper for converting classes into JSON strings
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KioskFacade kioskFacade;

    private final Faker faker = new Faker();

    @Test
    void contextLoads() {
    }

    @Test
    void borrowInvalidPayload() throws Exception {
        KioskBorrowDto kioskBorrowDto = fakeKioskBorrowingDto(faker);
        kioskBorrowDto.setUserId("");

        given(kioskFacade.borrowBook(any(KioskBorrowDto.class)))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/kiosk/borrow").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(kioskBorrowDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void borrowSuccessful() throws Exception {
        // Todo after library: Book Management layers merged
        ;
    }

    @Test
    void returnNonExistingBookInstance() throws Exception {
        String bookInstanceId = UUID.randomUUID().toString();

        doThrow(EntityNotFoundException.class).when(kioskFacade).returnBook(any());

        mockMvc.perform(post("/kiosk/return/" + bookInstanceId))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnSuccessful() throws Exception {
        String bookInstanceId = UUID.randomUUID().toString();

        doNothing().when(kioskFacade).returnBook(bookInstanceId);

        mockMvc.perform(post("/kiosk/return/" + bookInstanceId))
                .andExpect(status().is2xxSuccessful());

    }
}
