package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Tests for user facade implementation.
 *
 * @author Marek Miček
 */
@WebMvcTest(controllers = {UserFacadeImpl.class, UserMapper.class})
public class UserFacadeImplTests {

    @MockBean
    private UserService userService;

    @Autowired
    private UserFacadeImpl userFacade;

    private final Faker faker = new Faker();

    @Test
    public void createUserSuccessful() {
        User user = fakeUser(faker, UserType.CLIENT);
        // mock
        given(userService.create(any(User.class))).willReturn(user);
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .username("random")
                .email("john.doe@gmail.com")
                .firstName("John")
                .lastName("Doe")
                .street(user.getStreet())
                .houseNumber(user.getHouseNumber())
                .city(user.getCity())
                .zip(user.getZip())
                .country(user.getCountry())
                .build();

        // perform
        UserDto result = userFacade.create(userCreateDto);

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getUserType()).isEqualTo(user.getUserType());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastName()).isEqualTo(user.getLastName());
        assertThat(result.getCountry()).isEqualTo(user.getCountry());
        assertThat(result.getCity()).isEqualTo(user.getCity());
        assertThat(result.getStreet()).isEqualTo(user.getStreet());
        assertThat(result.getHouseNumber()).isEqualTo(user.getHouseNumber());
        assertThat(result.getZip()).isEqualTo(user.getZip());
    }

    @Test
    public void updateUserSuccessful() {
        // create user
        User user = fakeUser(faker, UserType.CLIENT);
        User newUser = fakeUser(faker, UserType.CLIENT);

        // mock
        given(userService.find(eq(user.getId()))).willReturn(user);
        given(userService.update(any(User.class))).willReturn(newUser);
        UserCreateDto updatedUserDto = UserCreateDto.builder()
                .username(newUser.getUsername())
                .firstName(newUser.getFirstName())
                .lastName(newUser.getLastName())
                .email(newUser.getEmail())
                .street(newUser.getStreet())
                .houseNumber(newUser.getHouseNumber())
                .city(newUser.getCity())
                .zip(newUser.getZip())
                .country(newUser.getCountry())
                .build();

        // perform
        UserDto result = userFacade.update(user.getId(), updatedUserDto);

        assertThat(result.getId()).isEqualTo(newUser.getId());
        assertThat(result.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(result.getUserType()).isEqualTo(newUser.getUserType());
        assertThat(result.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(result.getFirstName()).isEqualTo(newUser.getFirstName());
        assertThat(result.getLastName()).isEqualTo(newUser.getLastName());
        assertThat(result.getCountry()).isEqualTo(newUser.getCountry());
        assertThat(result.getCity()).isEqualTo(newUser.getCity());
        assertThat(result.getStreet()).isEqualTo(newUser.getStreet());
        assertThat(result.getHouseNumber()).isEqualTo(newUser.getHouseNumber());
        assertThat(result.getZip()).isEqualTo(newUser.getZip());
    }


    @Test
    public void updateUserNonExistentId() {
        // mock
        given(userService.find(eq("non-existent"))).willThrow(NotFoundException.class);

        // perform
        UserCreateDto userCreateDto = UserCreateDto.builder().build();
        assertThrows(NotFoundException.class, () -> userFacade.update("non-existent", userCreateDto));
    }

    @Test
    public void deleteUserSuccessful() {
        // create user
        User user = fakeUser(faker, UserType.CLIENT);

        // mock
        given(userService.find(eq(user.getId()))).willReturn(user);

        // perform
        userFacade.delete(user.getId());
        verify(userService, atLeastOnce()).delete(eq(user));
    }

    @Test
    public void deleteUserNonExistentId() {
        // mock
        given(userService.find(eq("non-existent"))).willThrow(NotFoundException.class);

        // perform
        assertThrows(NotFoundException.class, () -> userFacade.delete("non-existent"));
    }
}
