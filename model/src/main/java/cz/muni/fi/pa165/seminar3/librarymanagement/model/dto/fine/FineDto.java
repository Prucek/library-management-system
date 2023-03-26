package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FineDto extends DomainObjectDto {

    @NotNull
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull
    private UserDto issuer;

    @NotNull
    private BorrowingDto outstandingBorrowing;

    @NotNull
    private PaymentDto resolvingPayment;
}

