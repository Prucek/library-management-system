package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.Fine;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserType;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BorrowingUtils.fakeBorrowing;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUser;

public class FineUtils {

    public static Fine fakeFine() {
        return fakeFine(new Faker());
    }

    public static Fine fakeFine(Faker faker) {
        return Fine.builder()
                .id(faker.internet().uuid())
                .amount(faker.number().randomDouble(2, 1, 100))
                .issuer(fakeUser(faker, UserType.LIBRARIAN))
                .outstandingBorrowing(fakeBorrowing(faker))
                .build();
    }
}
