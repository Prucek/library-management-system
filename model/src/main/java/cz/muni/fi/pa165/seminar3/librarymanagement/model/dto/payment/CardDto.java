package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for a card with which the payment will be made. Data Transfer Object that is stable for API.
 */
@Getter
@Setter
public class CardDto extends DomainObjectDto {
    public String cardNumber;
    public String expiration;
    public String cvv2;
}
