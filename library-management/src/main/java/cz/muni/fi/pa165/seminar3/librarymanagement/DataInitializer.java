package cz.muni.fi.pa165.seminar3.librarymanagement;

import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.BorrowingService;
import cz.muni.fi.pa165.seminar3.librarymanagement.reservation.Reservation;
import cz.muni.fi.pa165.seminar3.librarymanagement.reservation.ReservationService;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.Address;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserType;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Component
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;

    private final ReservationService reservationService;

    private final BorrowingService borrowingService;

    public DataInitializer(UserService userService, ReservationService reservationService, BorrowingService borrowingService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.borrowingService = borrowingService;
    }

    @Override
    public void run(ApplicationArguments args) {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .email("test@email.com")
                .firstName("John")
                .lastName("Doe")
                .username("johnD")
                .password("password")
                .userType(UserType.CLIENT)
                .address(Address.builder().city("Brno").country("CZ").street("Hrnčírska").houseNumber("99").build())
                .build();

        userService.create(user);

        Borrowing borrowing = Borrowing.builder()
                .id(UUID.randomUUID().toString())
                .from(LocalDateTime.now())
                .to(LocalDateTime.now().plus(5, ChronoUnit.DAYS))
                .user(user)
                .build();

        borrowingService.create(borrowing);

        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID().toString())
                .from(LocalDateTime.now())
                .to(LocalDateTime.now().plus(10, ChronoUnit.DAYS))
                .user(user)
                .build();

        reservationService.create(reservation);
    }
}