package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BorrowingUtils.fakeBorrowing;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BorrowingUtils.fakeBorrowingDto;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUser;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUserDto;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.Fine;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;

/**
 * Class containing fine test utility methods.
 *
 * @author Juraj Marcin
 */
public class FineUtils {

    /**
     * Generates a fake fine entity.
     *
     * @param faker faker instance
     * @return fine entity
     */
    public static Fine fakeFine(Faker faker) {
        return Fine.builder()
                .id(faker.internet().uuid())
                .amount(faker.number().randomDouble(2, 1, 100))
                .issuer(fakeUser(faker, UserType.LIBRARIAN))
                .outstandingBorrowing(fakeBorrowing(faker))
                .resolvingPayment(null)
                .build();
    }

    /**
     * Generates a fake fine dto.
     *
     * @param faker faker instance
     * @return fine dto
     */
    public static FineDto fakeFineDto(Faker faker) {
        return FineDto.builder()
                .id(faker.internet().uuid())
                .amount(faker.number().randomDouble(2, 1, 100))
                .issuer(fakeUserDto(faker, UserType.LIBRARIAN))
                .outstandingBorrowing(fakeBorrowingDto(faker))
                .build();
    }
}
