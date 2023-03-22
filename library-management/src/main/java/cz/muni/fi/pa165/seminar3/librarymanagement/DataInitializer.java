package cz.muni.fi.pa165.seminar3.librarymanagement;

import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.BorrowingService;
import cz.muni.fi.pa165.seminar3.librarymanagement.reservation.Reservation;
import cz.muni.fi.pa165.seminar3.librarymanagement.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationRunner {

    private final ReservationService reservationService;

    private final BorrowingService borrowingService;

    @Override
    public void run(ApplicationArguments args) {
        Borrowing borrowing = Borrowing.builder()
                        .from(LocalDateTime.now())
                        .to(LocalDateTime.now().plus(5, ChronoUnit.DAYS))
                        .build();

        borrowingService.create(borrowing);

        Reservation reservation = Reservation.builder()
                .from(LocalDateTime.now())
                .to(LocalDateTime.now().plus(10, ChronoUnit.DAYS))
                .build();

        reservationService.create(reservation);
    }
}
