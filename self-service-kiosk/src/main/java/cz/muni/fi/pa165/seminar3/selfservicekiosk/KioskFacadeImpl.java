package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings.SettingsDto;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Class representing kiosk facade.
 *
 * @author Marek Fiala
 */

@Service
public class KioskFacadeImpl implements KioskFacade {

    private final LibraryManagementApi libraryManagementApi;

    /**
     * Creates a new Kiosk facade implementation instance.
     *
     * @param libraryManagementApi library management microservice API client instance
     */
    @Autowired
    public KioskFacadeImpl(LibraryManagementApi libraryManagementApi) {
        this.libraryManagementApi = libraryManagementApi;
    }

    @Override
    public BorrowingDto borrowBook(KioskBorrowDto kioskBorrowDto) {
        if (kioskBorrowDto.getUserId() == null || kioskBorrowDto.getUserId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing user id.");

        }
        if (kioskBorrowDto.getBookInstanceId() == null || kioskBorrowDto.getBookInstanceId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing book instance id.");
        }

        SettingsDto settings = libraryManagementApi.getSettings();

        return libraryManagementApi.createBorrowing(BorrowingCreateDto.builder()
                .userId(kioskBorrowDto.getUserId())
                .bookInstanceId(kioskBorrowDto.getBookInstanceId())
                .from(LocalDateTime.now())
                .to(LocalDateTime.now().plus(settings.getBorrowingLimit(), ChronoUnit.DAYS))
                .build());
    }

    @Override
    public void returnBook(String bookInstanceId) {
        BorrowingDto borrowingDto = libraryManagementApi.getPendingBorrowing(bookInstanceId);

        libraryManagementApi.updateBorrowing(borrowingDto.getId(), BorrowingCreateDto.builder()
                .userId(borrowingDto.getUser().getId())
                .bookInstanceId(borrowingDto.getBookInstance().getId())
                .from(borrowingDto.getFrom())
                .to(borrowingDto.getTo())
                .fineId(borrowingDto.getFine() != null ? borrowingDto.getFine().getId() : null)
                .returned(LocalDateTime.now())
                .build());
    }
}
