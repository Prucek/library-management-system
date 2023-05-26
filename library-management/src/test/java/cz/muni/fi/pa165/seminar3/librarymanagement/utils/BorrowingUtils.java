package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBookInstance;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBookInstanceDto;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUser;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUserDto;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

/**
 * Class containing borrowing test utility methods.
 *
 * @author Juraj Marcin
 */
public class BorrowingUtils {
    /**
     * Generates a fake borrowing entity.
     *
     * @param faker faker instance
     * @return borrowing entity
     */
    public static Borrowing fakeBorrowing(Faker faker) {
        return Borrowing.builder()
                .id(faker.internet().uuid())
                .borrowedFrom(faker.date()
                        .past(60, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .borrowedTo(faker.date()
                        .future(30, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .returned(faker.date()
                        .future(60, 30, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .bookInstance(fakeBookInstance(faker))
                .user(fakeUser(faker, UserType.CLIENT))
                .fine(null)
                .build();
    }

    /**
     * Generates a fake borrowing dto.
     *
     * @param faker faker instance
     * @return borrowing dto
     */
    public static BorrowingDto fakeBorrowingDto(Faker faker) {
        return BorrowingDto.builder()
                .id(faker.internet().uuid())
                .borrowedFrom(faker.date()
                        .past(60, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .borrowedTo(faker.date()
                        .future(30, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .returned(faker.date()
                        .future(60, 30, TimeUnit.DAYS)
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .bookInstance(fakeBookInstanceDto(faker))
                .user(fakeUserDto(faker, UserType.CLIENT))
                .build();
    }
}
