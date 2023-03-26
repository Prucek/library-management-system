package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

/**
 * DTO for a transaction. Data Transfer Object that is stable for API.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto extends DomainObjectDto {

    private double amount;
    private TransactionStatus status = TransactionStatus.WAITING;
    private String callbackURL;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TransactionDto transactionDto)) {
            return false;
        }
        return transactionDto.getAmount() == this.getAmount() && transactionDto.getStatus() == this.getStatus()
                && Objects.equals(transactionDto.getCallbackURL(), this.getCallbackURL());
    }
}
