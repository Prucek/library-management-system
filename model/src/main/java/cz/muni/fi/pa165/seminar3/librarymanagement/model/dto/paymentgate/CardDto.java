package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for a card with which the payment will be made. Data Transfer Object that is stable for API.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto extends DomainObjectDto {
    public String cardNumber;
    public String expiration;
    public String cvv2;
}
