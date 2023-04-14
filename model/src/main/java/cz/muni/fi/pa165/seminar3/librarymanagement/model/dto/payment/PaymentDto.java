package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class representing payment dto.
 *
 * @author Juraj Marcin
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto extends DomainObjectDto {

    @NotBlank
    private String transactionId;

    @NotNull
    private PaymentStatus status;

    @NotNull
    private List<FineDto> paidFines;
}
