package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.address.AddressDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import cz.muni.fi.pa165.seminar3.librarymanagement.address.Address;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;

/**
 * Class containing user test utility methods.
 *
 * @author Juraj Marcin
 */
public class UserUtils {

    /**
     * Generates a fake address entity.
     *
     * @param faker faker instance
     * @return address entity
     */
    public static Address fakeAddress(Faker faker) {
        return Address.builder()
                .street(faker.address().streetName())
                .houseNumber(faker.address().streetAddressNumber())
                .city(faker.address().city())
                .zip(faker.address().zipCode())
                .country(faker.address().country())
                .build();
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
     * Generates a fake user entity.
     *
     * @param faker faker instance
     * @param type  type of the user
     * @return user entity
     */
    public static User fakeUser(Faker faker, UserType type) {
        return User.builder()
                .id(faker.internet().uuid())
                .userType(type)
                .email(faker.internet().emailAddress())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .address(fakeAddress(faker))
                .username(faker.name().username())
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
}
