package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for a created user. Data Transfer Object that is stable for API,
 * independent of internal User class.
 */
@Getter
@Setter
@SuperBuilder
public class UserCreateDto {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private AddressDto address;
}
