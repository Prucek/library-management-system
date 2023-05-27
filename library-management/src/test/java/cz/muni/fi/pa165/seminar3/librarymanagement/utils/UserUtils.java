package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import java.util.Collections;

/**
 * Class containing user test utility methods.
 *
 * @author Juraj Marcin
 */
public class UserUtils {

    /**
     * Generates a fake user entity.
     *
     * @param faker faker instance
     * @param type  type of the user
     * @return user entity
     */
    public static User fakeUser(Faker faker, UserType type) {
        Address address = faker.address();
        return User.builder()
                .id(faker.internet().uuid())
                .userType(type)
                .email(faker.internet().emailAddress())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .username(faker.name().username())
                .street(address.streetName())
                .houseNumber(address.streetAddressNumber())
                .city(address.city())
                .zip(address.zipCode())
                .country(address.country())
                .borrowings(Collections.emptyList())
                .issuedFines(Collections.emptyList())
                .reservations(Collections.emptyList())
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
        Address address = faker.address();
        return UserDto.builder()
                .id(faker.internet().uuid())
                .userType(type)
                .email(faker.internet().emailAddress())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .username(faker.name().username())
                .street(address.streetName())
                .houseNumber(address.streetAddressNumber())
                .city(address.city())
                .zip(address.zipCode())
                .country(address.country())
                .build();
    }
}
