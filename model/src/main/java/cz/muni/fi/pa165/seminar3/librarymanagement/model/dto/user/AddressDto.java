package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for an address. Data Transfer Object that is stable for API,
 * independent of internal Address class.
 */
@Getter
@Setter
@SuperBuilder
public class AddressDto {

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String zip;
}

