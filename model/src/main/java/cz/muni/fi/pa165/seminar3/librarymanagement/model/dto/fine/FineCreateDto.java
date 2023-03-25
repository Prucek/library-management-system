package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class FineCreateDto {

    @NotNull
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotBlank
    private String outstandingBorrowingId;

    @NotBlank
    private String issuerId;
}
