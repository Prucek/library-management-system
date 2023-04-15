package cz.muni.fi.pa165.seminar3.librarymanagement.address;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.Entity;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class representing Address entity.
 *
 * @author Peter Rúček
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address extends DomainObject {

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String zip;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address address)) {
            return false;
        }
        return Objects.equals(country, address.country) && Objects.equals(city, address.city) && Objects.equals(street,
                address.street) && Objects.equals(houseNumber, address.houseNumber) && Objects.equals(zip, address.zip);
    }
}
