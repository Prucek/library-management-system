package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for a created user. Data Transfer Object that is stable for API,
 * independent of internal User class.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private AddressDto address;
}
