package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank
    private String cardNumber;

    @NotBlank
    @Pattern(regexp = "^\\d{2}/\\d{2}$")
    private String expiration;

    @NotBlank
    @Pattern(regexp = "^\\d{3,4}$")
    private String cvv2;
}
