package cz.muni.fi.pa165.seminar3.reporting;

import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.USER_SCOPE;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting.BookReportDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting.FinanceReportDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reporting.UserReportDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import cz.muni.fi.pa165.seminar3.reporting.service.ReportController;
import cz.muni.fi.pa165.seminar3.reporting.service.ReportService;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@WebMvcTest(ReportController.class)
class ReportingControllerTests {

    // injected mock implementation of Spring MVC
    @Autowired
    private MockMvc mockMvc;

    // injected mock implementation of the ReportController
    @MockBean
    private ReportService reportService;

    @Test
    void contextLoads() {
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_" + LIBRARIAN_SCOPE, "SCOPE_" + USER_SCOPE})
    void getBookReport() throws Exception {
        String id = "is7sdf6a8ys4dhf78dfy";
        UserDto user = new UserDto();
        user.setId(id);
        user.setFirstName("James");
        user.setLastName("Bond");

        BookReportDto bookDto = new BookReportDto();
        bookDto.setGeneratedAt(Instant.now());
        bookDto.setBorrowedBooksCount(6);
        bookDto.setReturnedBooksCount(3);
        bookDto.setUser(user);

        // define what mock service returns when called
        given(reportService.generateBookReport(id)).willReturn(bookDto);

        // call controller and check the result
        mockMvc.perform(get("/reports/books/" + id).header("User-Agent", "007").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.generatedAt").value(bookDto.getGeneratedAt().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(user.getId()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.borrowedBooksCount").value(bookDto.getBorrowedBooksCount()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.returnedBooksCount").value(bookDto.getReturnedBooksCount()));
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_" + LIBRARIAN_SCOPE, "SCOPE_" + USER_SCOPE})
    void getFinanceReport() throws Exception {
        String id = "is7sdf6a8ys4dhf78dfy";
        UserDto user = new UserDto();
        user.setId(id);
        user.setFirstName("James");
        user.setLastName("Bond");

        FinanceReportDto financeDto = new FinanceReportDto();
        financeDto.setGeneratedAt(Instant.now());
        financeDto.setFinesCount(6);
        financeDto.setFinesTotalPaid(3);
        financeDto.setUser(user);

        // define what mock service returns when called
        given(reportService.generateFinanceReport(id)).willReturn(financeDto);

        // call controller and check the result
        mockMvc.perform(get("/reports/finances/" + id).header("User-Agent", "007").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.generatedAt").value(financeDto.getGeneratedAt().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finesCount").value(financeDto.getFinesCount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.finesTotalPaid").value(financeDto.getFinesTotalPaid()));
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_" + LIBRARIAN_SCOPE, "SCOPE_" + USER_SCOPE})
    void getUserReport() throws Exception {
        Instant instant = Instant.now();
        int usersCount = 6;
        int librarianCount = 3;
        int newUsersCount = 2;

        UserReportDto userDto = new UserReportDto();
        userDto.setGeneratedAt(instant);
        userDto.setUsersCount(usersCount);
        userDto.setLibrarianCount(librarianCount);
        userDto.setNewUserCount(newUsersCount);

        // define what mock service returns when called
        given(reportService.generateUserReport()).willReturn(userDto);

        // call controller and check the result
        mockMvc.perform(get("/reports/users").header("User-Agent", "007").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.generatedAt").value(instant.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.usersCount").value(usersCount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.librarianCount").value(librarianCount))
                .andExpect(MockMvcResultMatchers.jsonPath("$.newUserCount").value(newUsersCount));
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_" + LIBRARIAN_SCOPE, "SCOPE_" + USER_SCOPE})
    void getNonexistentFinanceReport() throws Exception {

        given(reportService.generateFinanceReport("-1")).willThrow(
                new WebClientResponseException(HttpStatus.NOT_FOUND, "Book not found", null, null, null, null));

        mockMvc.perform(get("/reports/finances/-1").header("User-Agent", "007").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_" + LIBRARIAN_SCOPE, "SCOPE_" + USER_SCOPE})
    void getNonexistentBookReport() throws Exception {
        given(reportService.generateBookReport("-1")).willThrow(
                new WebClientResponseException(HttpStatus.NOT_FOUND, "Book not found", null, null, null, null));

        mockMvc.perform(get("/reports/books/-1").header("User-Agent", "007").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
