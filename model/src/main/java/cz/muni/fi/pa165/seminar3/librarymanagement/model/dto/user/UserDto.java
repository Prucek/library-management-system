package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for a user. Data Transfer Object that is stable for API,
 * independent of internal User class.
 */
@Getter
@Setter
public class UserDto extends DomainObjectDto {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private AddressDto address;
}

