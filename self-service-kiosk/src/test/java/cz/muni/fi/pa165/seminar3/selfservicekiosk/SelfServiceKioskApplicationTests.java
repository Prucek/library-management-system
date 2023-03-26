package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskReturnDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KioskController.class)
class SelfServiceKioskApplicationTests {

    // injected mock implementation of Spring MVC
    @Autowired
    private MockMvc mockMvc;

    // injected mapper for converting classes into JSON strings
    @Autowired
    private ObjectMapper objectMapper;

    // injected mock implementation of the KioskController
    @MockBean
    private KioskController kioskController;


    @Test
    void contextLoads() {
    }

    @Test
    void borrowNonExistingBook() throws Exception {
        KioskBorrowDto kioskBorrowing = new KioskBorrowDto();
        kioskBorrowing.setBookInstanceId("-1"); kioskBorrowing.setUserId("456");

        given(kioskController.borrow(kioskBorrowing)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Book instance with id:-1 not found"));

        mockMvc.perform(post("/kiosk/borrow")
                        .header("User-Agent", "007")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kioskBorrowing))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void borrowForNonExistingUser() throws Exception {
        KioskBorrowDto kioskBorrowing = new KioskBorrowDto();
        kioskBorrowing.setBookInstanceId("123"); kioskBorrowing.setUserId("-1");

        given(kioskController.borrow(kioskBorrowing)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with id:-1 not found"));

        mockMvc.perform(post("/kiosk/borrow")
                        .header("User-Agent", "007")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kioskBorrowing))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void borrowExistingBook() throws Exception {
        KioskBorrowDto kioskBorrowing = new KioskBorrowDto();
        String exampleBookInstanceId = "123";
        String exampleUserId = "456";
        kioskBorrowing.setBookInstanceId(exampleBookInstanceId);
        kioskBorrowing.setUserId(exampleUserId);

        BorrowingDto newBorrowing = new BorrowingDto();
        newBorrowing.setId(UUID.randomUUID().toString());
        newBorrowing.setBookInstance(new BookInstanceDto());
        newBorrowing.setUser(new UserDto());
        newBorrowing.setFrom(LocalDateTime.now());
        newBorrowing.setTo(LocalDateTime.now().plus(30, ChronoUnit.DAYS));

        given(kioskController.borrow(kioskBorrowing)).willReturn(newBorrowing);

        mockMvc.perform(post("/kiosk/borrow")
                        .header("User-Agent", "007")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kioskBorrowing))
                )
                .andExpect(status().isAccepted());

    }

    @Test
    void returnNonExistingBook() throws Exception {
        KioskReturnDto kioskReturnDto = new KioskReturnDto();
        kioskReturnDto.setBookInstanceId("-1");

        given(kioskController.returnBook(kioskReturnDto)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(post("/kiosk/return")
                        .header("User-Agent", "007")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kioskReturnDto))
                )
                .andExpect(status().isNotFound());

    }

    @Test
    void returnExistingBook() throws Exception {
        KioskReturnDto kioskReturnDto = new KioskReturnDto();
        String exampleBookInstanceId = "123";
        kioskReturnDto.setBookInstanceId(exampleBookInstanceId);

        BorrowingDto newBorrowing = new BorrowingDto();
        newBorrowing.setId(UUID.randomUUID().toString());
        newBorrowing.setBookInstance(new BookInstanceDto());
        newBorrowing.setUser(new UserDto());
        newBorrowing.setFrom(LocalDateTime.now());
        newBorrowing.setTo(LocalDateTime.now().plus(30, ChronoUnit.DAYS));

        given(kioskController.returnBook(kioskReturnDto)).willReturn(newBorrowing);

        mockMvc.perform(post("/kiosk/return")
                        .header("User-Agent", "007")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(kioskReturnDto))
                )
                .andExpect(status().isAccepted());

    }
}
