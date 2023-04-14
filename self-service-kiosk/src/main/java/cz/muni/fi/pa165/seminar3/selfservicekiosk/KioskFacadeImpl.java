package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
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

    @Override
    public BorrowingDto borrowBook(KioskBorrowDto kioskBorrowDto) {

        // Invoke Post on http://127.0.0.1:8080/borrowings

        if (kioskBorrowDto.getUserId() == null || kioskBorrowDto.getUserId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing user id.");

        }
        if (kioskBorrowDto.getBookInstanceId() == null || kioskBorrowDto.getBookInstanceId().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing book instance id.");
        }

        // Mock for now
        BorrowingDto newBorrowing = new BorrowingDto();
        newBorrowing.setId(UUID.randomUUID().toString());
        newBorrowing.setBookInstance(new BookInstanceDto());
        newBorrowing.setUser(new UserDto());
        newBorrowing.setFrom(LocalDateTime.now());
        newBorrowing.setTo(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
        return newBorrowing;
    }

    @Override
    public void returnBook(String bookInstanceId) {
        // Invoke Delete on http://127.0.0.1:8080/borrowings/{id}
        ;
    }
}
