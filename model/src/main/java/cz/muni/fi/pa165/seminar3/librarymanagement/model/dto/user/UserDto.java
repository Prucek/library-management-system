package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for a user. Data Transfer Object that is stable for API,
 * independent of internal User class.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends DomainObjectDto {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private AddressDto address;

    private UserType userType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDto userDto)) {
            return false;
        }
        return Objects.equals(username, userDto.username) && Objects.equals(email, userDto.email) && Objects.equals(
                firstName, userDto.firstName) && Objects.equals(lastName, userDto.lastName) && Objects.equals(address,
                userDto.address);
    }
}

