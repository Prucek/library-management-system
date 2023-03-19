package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends DomainObjectDto {

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private AddressDto address;
}

