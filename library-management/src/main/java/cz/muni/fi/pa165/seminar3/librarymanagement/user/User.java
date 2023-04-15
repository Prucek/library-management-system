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
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;


/**
 * Class representing User entity.
 *
 * @author Peter Rúček
 */
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
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Address> addresses;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }
        return Objects.equals(username, user.username) && userType == user.userType && Objects.equals(password,
                user.password) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName) && Objects.equals(addresses, user.addresses);
    }

}
