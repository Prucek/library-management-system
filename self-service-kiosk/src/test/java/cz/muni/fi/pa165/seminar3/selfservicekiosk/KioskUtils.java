package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.address.AddressDto;
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
     * Generates a fake address dto.
     *
     * @param faker faker instance
     * @return address dto
     */
    public static AddressDto fakeAddressDto(Faker faker) {
        return AddressDto.builder()
                .street(faker.address().streetName())
                .houseNumber(faker.address().streetAddressNumber())
                .city(faker.address().city())
                .zip(faker.address().zipCode())
                .country(faker.address().country())
                .build();
    }

    /**
     * Generates a fake user dto.
     *
     * @param faker faker instance
     * @param type  type of the user
     * @return user dto
     */
    public static UserDto fakeUserDto(Faker faker, UserType type) {
        return UserDto.builder()
                .id(faker.internet().uuid())
                .userType(type)
                .email(faker.internet().emailAddress())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .address(fakeAddressDto(faker))
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
