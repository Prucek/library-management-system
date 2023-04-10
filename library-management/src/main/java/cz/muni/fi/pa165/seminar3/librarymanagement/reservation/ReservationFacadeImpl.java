package cz.muni.fi.pa165.seminar3.librarymanagement.reservation;

import cz.muni.fi.pa165.seminar3.librarymanagement.book.Book;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookService;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class ReservationFacadeImpl extends DomainFacadeImpl<Reservation, ReservationDto, ReservationCreateDto>
        implements ReservationFacade {

    @Getter
    private final ReservationService domainService;
    @Getter
    private final ReservationMapper domainMapper;
    private final UserService userService;
    private final BookService bookService;

    public ReservationFacadeImpl(ReservationService domainService, ReservationMapper domainMapper,
                                 UserService userService, BookService bookService) {
        this.domainService = domainService;
        this.domainMapper = domainMapper;
        this.userService = userService;
        this.bookService = bookService;
    }

    /**
     * @param reservationCreateDto dto to create from
     * @return                     created reservation DTO
     */
    @Override
    public ReservationDto create(ReservationCreateDto reservationCreateDto) {
        User user = userService.find(reservationCreateDto.getIssuerId());
        Book book = bookService.find(reservationCreateDto.getBookID());

        Reservation reservation = Reservation.builder()
                .from(reservationCreateDto.getFrom())
                .to(reservationCreateDto.getTo())
                .user(user)
                .book(book)
                .build();

        return domainMapper.toDto(reservation);
    }

    /**
     * @param id                   id of reservation to update
     * @param reservationCreateDto new reservation values
     * @return                     updated reservation DTO
     */
    @Override
    public ReservationDto updateReservation(String id, ReservationCreateDto reservationCreateDto) {
        Reservation reservation = domainService.find(id);
        User user = userService.find(reservationCreateDto.getIssuerId());
        Book book = bookService.find(reservationCreateDto.getBookID());

        reservation.setTo(reservationCreateDto.getTo());
        reservation.setFrom(reservationCreateDto.getFrom());
        reservation.setUser(user);
        reservation.setBook(book);

        return domainMapper.toDto(domainService.update(reservation));
    }

    /**
     * @param reservationId id to delete
     */
    @Override
    public void deleteReservation(String reservationId) {
        Reservation reservation = domainService.find(reservationId);
        domainService.delete(reservation);
    }
}
