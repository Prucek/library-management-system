package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.Fine;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import cz.muni.fi.pa165.seminar3.librarymanagement.reservation.Reservation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "users")
public class User extends DomainObject {

    private String username;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String email;

    private String firstName;

    private String lastName;

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String zip;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "issuer")
    private List<Fine> issuedFines;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Borrowing> borrowings;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations;

    @PreRemove
    private void preRemove() {
        issuedFines.forEach(fine -> fine.setIssuer(null));
        borrowings.forEach(borrowing -> borrowing.setUser(null));
    }
}
