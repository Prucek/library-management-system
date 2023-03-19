package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String zip;
}
