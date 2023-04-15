package cz.muni.fi.pa165.seminar3.reporting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import cz.muni.fi.pa165.seminar3.reporting.service.BookReportDto;
import cz.muni.fi.pa165.seminar3.reporting.service.FinanceReportDto;
import cz.muni.fi.pa165.seminar3.reporting.service.ReportService;
import cz.muni.fi.pa165.seminar3.reporting.service.UserReportDto;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ReportingServiceTests {

    @MockBean
    private ReportService reportService;

    @Test
    void generateBookReport() {

        String id = "randomId";
        int borrowed = 4;
        int returned = 2;
        UserDto userDto = getUserDto(id);

        List<BorrowingDto> borrowings = List.of(
                BorrowingDto.builder().build(),
                BorrowingDto.builder().build(),
                BorrowingDto.builder().to(LocalDateTime.now()).build(),
                BorrowingDto.builder().to(LocalDateTime.now()).build()
        );

        // define what mock service returns when called
        given(reportService.getUserDto(id)).willReturn(userDto);
        given(reportService.getUserBorrowings(id)).willReturn(borrowings);
        given(reportService.generateBookReport(id)).willCallRealMethod();

        // call service and check the result
        BookReportDto report = reportService.generateBookReport(id);
        assertThat(report.getBorrowedBooksCount()).isEqualTo(borrowed);
        assertThat(report.getReturnedBooksCount()).isEqualTo(returned);
    }

    @Test
    void generateBookReportWith0Borrowings() {

        String id = "randomId";
        int borrowed = 0;
        int returned = 0;
        UserDto userDto = getUserDto(id);

        List<BorrowingDto> borrowings = List.of();

        // define what mock service returns when called
        given(reportService.getUserDto(id)).willReturn(userDto);
        given(reportService.getUserBorrowings(id)).willReturn(borrowings);
        given(reportService.generateBookReport(id)).willCallRealMethod();

        // call service and check the result
        BookReportDto report = reportService.generateBookReport(id);
        assertThat(report.getBorrowedBooksCount()).isEqualTo(borrowed);
        assertThat(report.getReturnedBooksCount()).isEqualTo(returned);
    }

    @Test
    void generateFinanceReport() {

        String id = "randomId";
        UserDto userDto = getUserDto(id);

        List<FineDto> fines = List.of(
                FineDto.builder().amount(10.).build(),
                FineDto.builder().amount(13.45).build(),
                FineDto.builder().amount(0.234).build()
        );

        // define what mock service returns when called
        given(reportService.getUserDto(id)).willReturn(userDto);
        given(reportService.getUserFines(id)).willReturn(fines);
        given(reportService.generateFinanceReport(id)).willCallRealMethod();

        // call service and check the result
        FinanceReportDto report = reportService.generateFinanceReport(id);
        assertThat(report.getFinesCount()).isEqualTo(fines.size());
        assertThat(report.getFinesTotalPaid()).isEqualTo(23.684);
    }

    @Test
    void generateFinanceReportWith0Fines() {

        String id = "randomId";
        UserDto userDto = getUserDto(id);

        List<FineDto> fines = List.of();

        // define what mock service returns when called
        given(reportService.getUserDto(id)).willReturn(userDto);
        given(reportService.getUserFines(id)).willReturn(fines);
        given(reportService.generateFinanceReport(id)).willCallRealMethod();

        // call service and check the result
        FinanceReportDto report = reportService.generateFinanceReport(id);
        assertThat(report.getFinesCount()).isEqualTo(fines.size());
        assertThat(report.getFinesTotalPaid()).isEqualTo(0);
    }

    @Test
    void generateFinanceReportWithNoAmount() {

        String id = "randomId";
        UserDto userDto = getUserDto(id);

        List<FineDto> fines = List.of(
                FineDto.builder().build(),
                FineDto.builder().amount(13.45).build(),
                FineDto.builder().amount(0.234).build()
        );

        // define what mock service returns when called
        given(reportService.getUserDto(id)).willReturn(userDto);
        given(reportService.getUserFines(id)).willReturn(fines);
        given(reportService.generateFinanceReport(id)).willCallRealMethod();

        // call service and check the result
        assertThrows(NullPointerException.class, () -> reportService.generateFinanceReport(id));
    }

    @Test
    void generateUserReport(){

        List<UserDto> users = List.of(
                UserDto.builder().userType(UserType.LIBRARIAN).createdAt(LocalDateTime.now()).build(),
                UserDto.builder().userType(UserType.LIBRARIAN).createdAt(LocalDateTime.now().minusDays(6)).build(),
                UserDto.builder().userType(UserType.CLIENT).createdAt(LocalDateTime.now().minusDays(1)).build(),
                UserDto.builder().userType(UserType.CLIENT).createdAt(LocalDateTime.now().minusDays(3)).build(),
                UserDto.builder().userType(UserType.CLIENT).createdAt(LocalDateTime.now().minusDays(4)).build()
        );

        // define what mock service returns when called
        given(reportService.getAllUsers()).willReturn(users);
        given(reportService.generateUserReport()).willCallRealMethod();

        // call service and check the result
        UserReportDto report = reportService.generateUserReport();
        assertThat(report.getUsersCount()).isEqualTo(3);
        assertThat(report.getLibrarianCount()).isEqualTo(2);
        assertThat(report.getNewUserCount()).isEqualTo(2);
    }

    @Test
    void generateUserReportWith0Users(){

        List<UserDto> users = List.of();

        // define what mock service returns when called
        given(reportService.getAllUsers()).willReturn(users);
        given(reportService.generateUserReport()).willCallRealMethod();

        // call service and check the result
        UserReportDto report = reportService.generateUserReport();
        assertThat(report.getUsersCount()).isEqualTo(0);
        assertThat(report.getLibrarianCount()).isEqualTo(0);
        assertThat(report.getNewUserCount()).isEqualTo(0);
    }

    private static UserDto getUserDto(String id) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setFirstName("James");
        userDto.setLastName("Bond");
        return userDto;
    }
}
