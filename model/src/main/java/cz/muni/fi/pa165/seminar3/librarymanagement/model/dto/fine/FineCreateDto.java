package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class representing fine create dto.
 *
 * @author Juraj Marcin
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FineCreateDto {

    private Double amount;

    @NotBlank
    private String outstandingBorrowingId;

    @NotBlank
    private String issuerId;
}
