package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.FineUtils.fakeFine;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.BorrowingService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.settings.Settings;
import cz.muni.fi.pa165.seminar3.librarymanagement.settings.SettingsService;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Tests for Fine facade implementation.
 *
 * @author Peter Rúček
 */
@WebMvcTest(controllers = {FineFacadeImpl.class, FineMapper.class})
public class FineFacadeImplTests {

    @MockBean
    private FineService fineService;

    @MockBean
    private UserService userService;

    @MockBean
    private BorrowingService borrowingService;

    @MockBean
    private SettingsService settingsService;

    @Autowired
    private FineFacadeImpl fineFacade;

    private final Faker faker = new Faker();

    @Test
    public void createFineSuccessful() {
        Fine fine = fakeFine(faker);

        // mock
        given(userService.find(fine.getIssuer().getId())).willReturn(fine.getIssuer());
        given(borrowingService.find(fine.getOutstandingBorrowing().getId())).willReturn(fine.getOutstandingBorrowing());
        given(settingsService.getCurrent()).willReturn(Settings.getDefault());
        given(fineService.create(any(Fine.class))).willReturn(fine);

        // perform
        FineCreateDto fineCreateDto = FineCreateDto.builder()
                .amount(fine.getAmount())
                .issuerId(fine.getIssuer().getId())
                .outstandingBorrowingId(fine.getOutstandingBorrowing().getId())
                .build();

        FineDto result = fineFacade.create(fineCreateDto);
        assertThat(result.getId()).isEqualTo(fine.getId());
        assertThat(result.getAmount()).isEqualTo(fine.getAmount());
        assertThat(result.getIssuer().getId()).isEqualTo(fine.getIssuer().getId());
        assertThat(result.getOutstandingBorrowing().getId()).isEqualTo(fine.getOutstandingBorrowing().getId());
    }

    @Test
    public void updateFineSuccessful() {
        // first create a fine
        Fine fine = fakeFine(faker);
        Fine newFine = fakeFine(faker);

        // mock
        given(userService.find(newFine.getIssuer().getId())).willReturn(newFine.getIssuer());
        given(borrowingService.find(newFine.getOutstandingBorrowing().getId())).willReturn(
                newFine.getOutstandingBorrowing());
        given(settingsService.getCurrent()).willReturn(Settings.getDefault());
        given(fineService.find(eq(fine.getId()))).willReturn(fine);
        given(fineService.update(any(Fine.class))).willReturn(newFine);

        // now update the fine
        FineCreateDto newCreateDto = FineCreateDto.builder()
                .amount(newFine.getAmount())
                .issuerId(newFine.getIssuer().getId())
                .outstandingBorrowingId(newFine.getOutstandingBorrowing().getId())
                .build();

        FineDto result = fineFacade.update(fine.getId(), newCreateDto);

        assertThat(result.getId()).isEqualTo(newFine.getId());
        assertThat(result.getAmount()).isEqualTo(newFine.getAmount());
        assertThat(result.getIssuer().getId()).isEqualTo(newFine.getIssuer().getId());
        assertThat(result.getOutstandingBorrowing().getId()).isEqualTo(newFine.getOutstandingBorrowing().getId());
    }

    @Test
    public void updateFineNonExistentId() {
        // mock
        given(fineService.find(eq("non-existent"))).willThrow(NotFoundException.class);

        FineCreateDto fineCreateDto = FineCreateDto.builder().build();
        assertThrows(NotFoundException.class, () -> fineFacade.update("non-existent", fineCreateDto));
    }

    @Test
    public void deleteFineSuccessful() {
        Fine fine = fakeFine(faker);

        // mock
        given(fineService.find(eq(fine.getId()))).willReturn(fine);

        // perform
        fineFacade.delete(fine.getId());
        verify(fineService, atLeastOnce()).delete(eq(fine));
    }

    @Test
    public void deleteFineNonExistentId() {
        // mock
        given(fineService.find(eq("non-existent"))).willThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> fineFacade.delete("non-existent"));
    }
}
