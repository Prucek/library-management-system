package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.address;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for an address. Data Transfer Object that is stable for API,
 * independent of internal Address class.
 *
 * @author Peter Rúček
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto extends DomainObjectDto {

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String zip;
}

