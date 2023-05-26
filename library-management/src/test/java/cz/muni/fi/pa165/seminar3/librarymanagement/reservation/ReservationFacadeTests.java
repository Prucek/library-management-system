package cz.muni.fi.pa165.seminar3.librarymanagement.reservation;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBook;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.ReservationUtils.fakeReservation;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.Book;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Tests for reservation facade.
 */
@WebMvcTest(controllers = {ReservationFacade.class, ReservationMapper.class})
public class ReservationFacadeTests {

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private UserService userService;

    @MockBean
    private BookService bookService;

    @Autowired
    private ReservationFacade reservationFacade;

    private final Faker faker = new Faker();

    @Test
    public void createSuccessful() {
        User user = fakeUser(faker, UserType.CLIENT);
        Book book = fakeBook(faker);
        Reservation reservation = fakeReservation(faker);

        // mock services
        given(userService.find(eq(user.getId()))).willReturn(user);
        given(bookService.find(eq(book.getId()))).willReturn(book);
        given(reservationService.create(any(Reservation.class))).willReturn(reservation);

        // perform test
        ReservationDto result = reservationFacade.create(ReservationCreateDto.builder()
                .reservedFrom(reservation.getReservedFrom())
                .reservedTo(reservation.getReservedTo())
                .userId(user.getId())
                .bookId(book.getId())
                .build());
        assertThat(result.getId()).isEqualTo(reservation.getId());
        assertThat(result.getReservedFrom()).isEqualTo(reservation.getReservedFrom());
        assertThat(result.getReservedTo()).isEqualTo(reservation.getReservedTo());
    }

    @Test
    public void createBookNotFound() {
        User user = fakeUser(faker, UserType.CLIENT);
        Book book = fakeBook(faker);
        Reservation reservation = fakeReservation(faker);

        // mock services
        given(userService.find(eq(user.getId()))).willReturn(user);
        given(bookService.find(eq(book.getId()))).willThrow(NotFoundException.class);

        // perform test
        assertThrows(NotFoundException.class, () -> reservationFacade.create(ReservationCreateDto.builder()
                .reservedFrom(reservation.getReservedFrom())
                .reservedTo(reservation.getReservedTo())
                .userId(user.getId())
                .bookId(book.getId())
                .build()));
    }

    @Test
    public void createUserNotFound() {
        User user = fakeUser(faker, UserType.CLIENT);
        Book book = fakeBook(faker);
        Reservation reservation = fakeReservation(faker);

        // mock services
        given(userService.find(eq(user.getId()))).willThrow(NotFoundException.class);
        given(bookService.find(eq(book.getId()))).willReturn(book);

        // perform test
        assertThrows(NotFoundException.class, () -> reservationFacade.create(ReservationCreateDto.builder()
                .reservedFrom(reservation.getReservedFrom())
                .reservedTo(reservation.getReservedTo())
                .userId(user.getId())
                .bookId(book.getId())
                .build()));
    }

    @Test
    public void updateSuccessful() {
        User user = fakeUser(faker, UserType.CLIENT);
        Book book = fakeBook(faker);
        Reservation reservation = fakeReservation(faker);

        // mock services
        given(userService.find(eq(user.getId()))).willReturn(user);
        given(bookService.find(eq(book.getId()))).willReturn(book);
        given(reservationService.find(eq(reservation.getId()))).willReturn(reservation);
        given(reservationService.update(eq(reservation))).willReturn(reservation);

        // perform test
        ReservationDto result = reservationFacade.updateReservation(reservation.getId(), ReservationCreateDto.builder()
                .reservedFrom(reservation.getReservedFrom())
                .reservedTo(reservation.getReservedTo())
                .userId(user.getId())
                .bookId(book.getId())
                .build());
        assertThat(result.getId()).isEqualTo(reservation.getId());
        assertThat(result.getReservedFrom()).isEqualTo(reservation.getReservedFrom());
        assertThat(result.getReservedTo()).isEqualTo(reservation.getReservedTo());
        assertThat(result.getUser().getId()).isEqualTo(user.getId());
        assertThat(result.getBook().getId()).isEqualTo(reservation.getBook().getId());
    }

    @Test
    public void updateBookNotFound() {
        User user = fakeUser(faker, UserType.CLIENT);
        Book book = fakeBook(faker);
        Reservation reservation = fakeReservation(faker);

        // mock services
        given(userService.find(eq(user.getId()))).willReturn(user);
        given(bookService.find(eq(book.getId()))).willThrow(NotFoundException.class);
        given(reservationService.find(eq(reservation.getId()))).willReturn(reservation);

        // perform test
        assertThrows(NotFoundException.class, () -> reservationFacade.updateReservation(reservation.getId(),
                ReservationCreateDto.builder()
                        .reservedFrom(reservation.getReservedFrom())
                        .reservedTo(reservation.getReservedTo())
                        .userId(user.getId())
                        .bookId(book.getId())
                        .build()));
    }

    @Test
    public void updateUserNotFound() {
        User user = fakeUser(faker, UserType.CLIENT);
        Book book = fakeBook(faker);
        Reservation reservation = fakeReservation(faker);

        // mock services
        given(userService.find(eq(user.getId()))).willThrow(NotFoundException.class);
        given(bookService.find(eq(book.getId()))).willReturn(book);
        given(reservationService.find(eq(reservation.getId()))).willReturn(reservation);

        // perform test
        assertThrows(NotFoundException.class, () -> reservationFacade.updateReservation(reservation.getId(),
                ReservationCreateDto.builder()
                        .reservedFrom(reservation.getReservedFrom())
                        .reservedTo(reservation.getReservedTo())
                        .userId(user.getId())
                        .bookId(book.getId())
                        .build()));
    }

    @Test
    public void updateNotFound() {
        User user = fakeUser(faker, UserType.CLIENT);
        Book book = fakeBook(faker);
        Reservation reservation = fakeReservation(faker);

        // mock services
        given(userService.find(eq(user.getId()))).willReturn(user);
        given(bookService.find(eq(book.getId()))).willReturn(book);
        given(reservationService.find(eq(reservation.getId()))).willThrow(NotFoundException.class);

        // perform test
        assertThrows(NotFoundException.class, () -> reservationFacade.updateReservation(reservation.getId(),
                ReservationCreateDto.builder()
                        .reservedFrom(reservation.getReservedFrom())
                        .reservedTo(reservation.getReservedTo())
                        .userId(user.getId())
                        .bookId(book.getId())
                        .build()));
    }

    @Test
    public void deleteSuccessful() {
        Reservation reservation = fakeReservation(faker);

        // mock services
        given(reservationService.find(eq(reservation.getId()))).willReturn(reservation);

        // perform test
        reservationFacade.deleteReservation(reservation.getId());
        verify(reservationService, atLeastOnce()).delete(eq(reservation));
    }

    @Test
    public void deleteNotFound() {
        Reservation reservation = fakeReservation(faker);

        // mock services
        given(reservationService.find(eq(reservation.getId()))).willThrow(NotFoundException.class);

        // perform test
        assertThrows(NotFoundException.class, () -> reservationFacade.deleteReservation(reservation.getId()));
    }
}
