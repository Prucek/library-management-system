package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.address.Address;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    @Singular
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Address> addresses;
}
