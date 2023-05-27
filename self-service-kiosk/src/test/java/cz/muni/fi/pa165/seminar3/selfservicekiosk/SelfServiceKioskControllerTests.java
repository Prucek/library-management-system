package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.selfservicekiosk.KioskUtils.fakeBookInstanceDto;
import static cz.muni.fi.pa165.seminar3.selfservicekiosk.KioskUtils.fakeKioskBorrowingDto;
import static cz.muni.fi.pa165.seminar3.selfservicekiosk.KioskUtils.fakeUserDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

/**
 * Tests for self-service kiosk controller.
 */
@WebMvcTest(KioskController.class)
class SelfServiceKioskControllerTests {

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
    @WithMockUser(authorities = {"SCOPE_" + LIBRARIAN_SCOPE})
    void borrowInvalidPayload() throws Exception {
        KioskBorrowDto kioskBorrowDto = fakeKioskBorrowingDto(faker);
        kioskBorrowDto.setUserId("");

        given(kioskFacade.borrowBook(any(KioskBorrowDto.class))).willThrow(
                new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/kiosk/borrow").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kioskBorrowDto))).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_" + LIBRARIAN_SCOPE})
    void borrowSuccessful() throws Exception {
        KioskBorrowDto kioskBorrowDto = fakeKioskBorrowingDto(faker);
        kioskBorrowDto.setUserId("random");

        UserDto fakeUserDto = fakeUserDto(faker, UserType.CLIENT);
        fakeUserDto.setId(kioskBorrowDto.getUserId());

        BookInstanceDto fakeBookInstanceDto = fakeBookInstanceDto(faker);
        fakeBookInstanceDto.setId(kioskBorrowDto.getBookInstanceId());

        BorrowingDto expectedResult = BorrowingDto.builder()
                .user(fakeUserDto)
                .bookInstance(fakeBookInstanceDto)
                .borrowedFrom(LocalDateTime.now())
                .borrowedTo(LocalDateTime.now().plus(10, ChronoUnit.DAYS))
                .build();

        given(kioskFacade.borrowBook(any(KioskBorrowDto.class))).willReturn(expectedResult);

        mockMvc.perform(post("/kiosk/borrow").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(kioskBorrowDto))).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_" + LIBRARIAN_SCOPE})
    void returnSuccessful() throws Exception {
        String bookInstanceId = UUID.randomUUID().toString();

        doNothing().when(kioskFacade).returnBook(bookInstanceId);

        mockMvc.perform(post("/kiosk/return/" + bookInstanceId)).andExpect(status().isOk());

    }
}
