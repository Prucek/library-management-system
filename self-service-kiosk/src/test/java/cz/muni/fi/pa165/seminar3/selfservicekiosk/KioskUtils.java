package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;

/**
 * Class containing kiosk test utility methods.
 *
 * @author Marek Fiala
 */
public class KioskUtils {

    /**
     * Generates a fake kiosk borrowing dto.
     *
     * @param faker faker instance
     * @return kiosk borrowing dto
     */
    public static KioskBorrowDto fakeKioskBorrowingDto(Faker faker) {
        return KioskBorrowDto.builder().bookInstanceId(faker.internet().uuid()).userId(faker.internet().uuid()).build();
    }

    /**
     * Generates a fake user dto.
     *
     * @param faker faker instance
     * @param type  type of the user
     * @return user dto
     */
    public static UserDto fakeUserDto(Faker faker, UserType type) {
        Address address = faker.address();
        return UserDto.builder()
                .id(faker.internet().uuid())
                .userType(type)
                .email(faker.internet().emailAddress())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .street(address.streetName())
                .houseNumber(address.streetAddressNumber())
                .city(address.city())
                .zip(address.zipCode())
                .country(address.country())
                .username(faker.name().username())
                .build();
    }

    /**
     * Generates a fake book instance dto.
     *
     * @param faker faker instance
     * @return book instance dto
     */
    public static BookInstanceDto fakeBookInstanceDto(Faker faker) {
        return BookInstanceDto.builder().id(faker.internet().uuid()).build();
    }
}
