package cz.muni.fi.pa165.seminar3.librarymanagement;

import cz.muni.fi.pa165.seminar3.librarymanagement.user.Address;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserType;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    @Override
    public void run(ApplicationArguments args) {
        User user = User.builder()
                .email("test@email.com")
                .firstName("John")
                .lastName("Doe")
                .username("johnD")
                .password("password")
                .userType(UserType.CLIENT).address(Address.builder()
                        .city("Brno")
                        .country("CZ")
                        .street("Hrnčírska")
                        .houseNumber("99")
                        .build())
                .build();

        userService.create(user);

    }
}
