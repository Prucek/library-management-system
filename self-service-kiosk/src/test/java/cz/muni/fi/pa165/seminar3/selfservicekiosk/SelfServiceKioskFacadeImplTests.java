package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings.SettingsDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static cz.muni.fi.pa165.seminar3.selfservicekiosk.KioskUtils.fakeKioskBorrowingDto;
import static cz.muni.fi.pa165.seminar3.selfservicekiosk.KioskUtils.fakeUserDto;
import static cz.muni.fi.pa165.seminar3.selfservicekiosk.KioskUtils.fakeBookInstanceDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Tests for self-service kiosk facade.
 *
 * @author Marek Miček
 */

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class SelfServiceKioskFacadeImplTests {

    @MockBean
    private LibraryManagementApi libraryManagementApi;

    @Autowired
    private KioskFacadeImpl kioskFacade;

    private final Faker faker = new Faker();

    @Test
    public void borrowBookSuccessful() {
        KioskBorrowDto kioskBorrowDto = fakeKioskBorrowingDto(faker);
        SettingsDto expectedSettings = SettingsDto.builder().borrowingPrice(25.0).borrowingLimit(90).build();

        given(libraryManagementApi.getSettings()).willReturn(expectedSettings);

        SettingsDto settings = libraryManagementApi.getSettings();

        BorrowingCreateDto borrowingCreateDto = BorrowingCreateDto.builder()
                .userId(kioskBorrowDto.getUserId())
                .bookInstanceId(kioskBorrowDto.getBookInstanceId())
                .from(LocalDateTime.now())
                .to(LocalDateTime.now().plus(settings.getBorrowingLimit(), ChronoUnit.DAYS))
                .build();

        UserDto fakeUserDto = fakeUserDto(faker, UserType.CLIENT);
        fakeUserDto.setId(borrowingCreateDto.getUserId());

        BookInstanceDto fakeBookInstanceDto = fakeBookInstanceDto(faker);
        fakeBookInstanceDto.setId(borrowingCreateDto.getBookInstanceId());

        BorrowingDto expectedResult = BorrowingDto.builder()
                .user(fakeUserDto)
                .bookInstance(fakeBookInstanceDto)
                .from(borrowingCreateDto.getFrom())
                .to(borrowingCreateDto.getTo())
                .build();

        given(libraryManagementApi.createBorrowing(any(BorrowingCreateDto.class))).willReturn(expectedResult);

        BorrowingDto result = kioskFacade.borrowBook(kioskBorrowDto);
        assertThat(result.getUser().getId()).isEqualTo(borrowingCreateDto.getUserId());
        assertThat(result.getBookInstance().getId()).isEqualTo(borrowingCreateDto.getBookInstanceId());
        assertThat(result.getFrom()).isEqualTo(borrowingCreateDto.getFrom());
        assertThat(result.getTo()).isEqualTo(borrowingCreateDto.getTo());
    }

    @Test
    public void returnBookSuccessful() {
        UserDto fakeUserDto = fakeUserDto(faker, UserType.CLIENT);
        BookInstanceDto fakeBookInstanceDto = fakeBookInstanceDto(faker);

        BorrowingDto borrowingDto = BorrowingDto.builder()
                .user(fakeUserDto)
                .bookInstance(fakeBookInstanceDto)
                .from(LocalDateTime.now())
                .to(LocalDateTime.now().plus(10, ChronoUnit.DAYS))
                .build();

        BorrowingDto updatedBorrowingDto = BorrowingDto.builder()
                .user(borrowingDto.getUser())
                .bookInstance(borrowingDto.getBookInstance())
                .from(borrowingDto.getFrom())
                .to(borrowingDto.getTo())
                .fine(borrowingDto.getFine() != null ? borrowingDto.getFine() : null)
                .returned(LocalDateTime.now())
                .build();

        given(libraryManagementApi.getPendingBorrowing(any(String.class)))
                .willReturn(borrowingDto);

        given(libraryManagementApi.updateBorrowing(eq(borrowingDto.getId()), any(BorrowingCreateDto.class)))
                .willReturn(updatedBorrowingDto);

        kioskFacade.returnBook(borrowingDto.getBookInstance().getId());
        verify(libraryManagementApi, atLeastOnce()).updateBorrowing(eq(borrowingDto.getId()), any(BorrowingCreateDto.class));
    }
}
