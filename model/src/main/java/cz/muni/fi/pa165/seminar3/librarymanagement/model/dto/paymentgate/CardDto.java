package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for a card with which the payment will be made. Data Transfer Object that is stable for API.
 *
 * @author Peter Rúček
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private String cardNumber;
    private String expiration;
    private String cvv2;
}
