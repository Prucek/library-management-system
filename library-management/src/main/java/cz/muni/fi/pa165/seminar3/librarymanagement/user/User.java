package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@NoArgsConstructor
@SuperBuilder
@Table(name = "domain_user")
public class User extends DomainObject {

    private String username;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    @Embedded
    private Address address;
}
