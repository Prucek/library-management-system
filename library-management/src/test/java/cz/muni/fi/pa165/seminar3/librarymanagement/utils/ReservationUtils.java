package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBook;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBookDto;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUser;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUserDto;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import cz.muni.fi.pa165.seminar3.librarymanagement.reservation.Reservation;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

/**
 * Class containing reservation test utility methods.
 *
 * @author Marek Miček
 */
public class ReservationUtils {

    /**
     * Generates a fake reservation entity.
     *
     * @param faker faker instance
     * @return reservation entity
     */
    public static Reservation fakeReservation(Faker faker) {
        return Reservation.builder()
                .id(faker.internet().uuid())
                .reservedFrom(faker.date()
                        .past(60, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .reservedTo(faker.date()
                        .future(30, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .book(fakeBook(faker))
                .user(fakeUser(faker, UserType.CLIENT))
                .build();
    }

    /**
     * Generates a fake reservation dto.
     *
     * @param faker faker instance
     * @return reservation dto
     */
    public static ReservationDto fakeReservationDto(Faker faker) {
        return ReservationDto.builder()
                .id(faker.internet().uuid())
                .reservedFrom(faker.date()
                        .past(60, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .reservedTo(faker.date()
                        .future(30, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .book(fakeBookDto(faker))
                .user(fakeUserDto(faker, UserType.CLIENT))
                .build();
    }
}
