package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import com.github.javafaker.Faker;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeAddressDto;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.address.AddressDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserFacadeImplTests {

    @Autowired
    private UserRepository domainRepository;

    @Autowired
    private UserMapper domainMapper;

    @Autowired
    private UserFacadeImpl userFacade;

    private final Faker faker = new Faker();

    @Test
    public void createUserSuccessful() {
        AddressDto addressDto = fakeAddressDto(faker);
        UserCreateDto userCreateDto = UserCreateDto.builder().username("random").email("john.doe@gmail.com")
                .password("empty").firstName("John").lastName("Doe").address(addressDto).build();

        UserDto result = userFacade.create(userCreateDto);

        assertThat(domainRepository.findById(result.getId())).isPresent();
    }

    @Test
    public void createEmptyUserSuccessful() {
        UserCreateDto userCreateDto = UserCreateDto.builder().build();
        UserDto result = userFacade.create(userCreateDto);

        assertThat(domainRepository.findById(result.getId())).isPresent();
    }

    @Test
    public void createNullUserThrowsException() {
        assertThrows(NullPointerException.class, () -> userFacade.create(null));

    }

    @Test
    public void updateUserSuccessful() {
        // create user
        UserDto createdUserDto = createUser();

        // update user
        User userToUpdate = domainRepository.findById(createdUserDto.getId()).orElse(null);
        assert userToUpdate != null;
        UserCreateDto updatedUserDto = UserCreateDto.builder()
                .username("new_user_name")
                .firstName(userToUpdate.getFirstName())
                .lastName(userToUpdate.getLastName())
                .email(userToUpdate.getEmail())
                .password(userToUpdate.getPassword())
                .address(fakeAddressDto(faker))
                .build();

        UserDto result = userFacade.update(createdUserDto.getId(), updatedUserDto);

        assertThat(domainRepository.findById(createdUserDto.getId())).isPresent();
        assertThat(domainMapper.fromDto(result).getUsername()).isEqualTo(updatedUserDto.getUsername());
    }

    @Test
    public void updateEmptyUserSuccessful() {
        // create user
        UserDto createdUserDto = createUser();

        // update user
        UserCreateDto updatedUserDto = UserCreateDto.builder().build();
        UserDto result = userFacade.update(createdUserDto.getId(), updatedUserDto);

        assertThat(domainRepository.findById(result.getId())).isPresent();
        assertThat(result.getUsername()).isEqualTo(updatedUserDto.getUsername());
        assertThat(result.getFirstName()).isEqualTo(updatedUserDto.getFirstName());
        assertThat(result.getLastName()).isEqualTo(updatedUserDto.getLastName());
        assertThat(result.getEmail()).isEqualTo(updatedUserDto.getEmail());
        assertThat(result.getAddresses()).isEqualTo(updatedUserDto.getAddresses());
    }

    @Test
    public void updateUserNullId() {

        UserCreateDto userCreateDto = UserCreateDto.builder().build();
        assertThrows(InvalidDataAccessApiUsageException.class, () -> userFacade.update(null, userCreateDto));
    }

    @Test
    public void updateFineNonExistentId() {

        UserCreateDto fineCreateDto = UserCreateDto.builder().build();
        assertThrows(EntityNotFoundException.class, () -> userFacade.update("non-existent", fineCreateDto));
    }

    @Test
    public void updateUserNullCreateDto() {

        // first create a user
        UserDto createdUserDto = createUser();

        // now update the user with null object
        UserCreateDto userCreateDto = null;
        assertThrows(NullPointerException.class, () -> userFacade.update(createdUserDto.getId(), userCreateDto));
    }

    @Test
    public void deleteUserSuccessful() {

        // first create a fine
        UserDto createdUserDto = createUser();

        // now delete the fine
        userFacade.delete(createdUserDto.getId());
        assertThat(domainRepository.findById(createdUserDto.getId())).isEmpty();
    }

    @Test
    public void deleteFineNullId() {

        assertThrows(InvalidDataAccessApiUsageException.class, () -> userFacade.delete(null));
    }

    @Test
    public void deleteFineNonExistentId() {

        assertThrows(EntityNotFoundException.class, () -> userFacade.delete("non-existent"));
    }

    private UserDto createUser() {
        AddressDto addressDto = fakeAddressDto(faker);
        UserCreateDto userCreateDto = UserCreateDto.builder().username("random").email("john.doe@gmail.com")
                .password("empty").firstName("John").lastName("Doe").address(addressDto).build();

        UserDto result = userFacade.create(userCreateDto);

        assertThat(domainRepository.findById(result.getId())).isPresent();

        return result;
    }

}
