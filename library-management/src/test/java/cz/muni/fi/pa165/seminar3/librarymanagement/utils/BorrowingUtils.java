package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserType;

import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUser;

public class BorrowingUtils {
    public static Borrowing fakeBorrowing() {
        return fakeBorrowing(new Faker());
    }

    public static Borrowing fakeBorrowing(Faker faker) {
        return Borrowing.builder()
                .id(faker.internet().uuid())
                .from(faker.date().past(60, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .to(faker.date().future(30, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .user(fakeUser(faker, UserType.CLIENT))
                .build();
    }
}
