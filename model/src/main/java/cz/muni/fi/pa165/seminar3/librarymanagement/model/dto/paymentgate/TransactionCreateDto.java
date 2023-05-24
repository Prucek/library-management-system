package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for a created transaction.
 *
 * @author Peter Rúček
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreateDto {

    @NotNull
    @DecimalMin("0.01")
    private double amount;

    @NotBlank
    private String callbackUrl;
}
