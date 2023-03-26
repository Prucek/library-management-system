package cz.muni.fi.pa165.seminar3.librarymanagement.utils;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.Address;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserType;

public class UserUtils {

    public static Address fakeAddress(Faker faker) {
        return Address.builder()
                .street(faker.address().streetName())
                .houseNumber(faker.address().streetAddressNumber())
                .city(faker.address().city())
                .zip(faker.address().zipCode())
                .country(faker.address().country())
                .build();
    }

    public static User fakeUser(UserType type) {
        return fakeUser(new Faker(), type);
    }

    public static User fakeUser(Faker faker,
                                UserType type) {
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
}
