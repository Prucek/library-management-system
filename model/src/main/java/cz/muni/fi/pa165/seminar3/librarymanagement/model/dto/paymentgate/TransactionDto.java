package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


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

    private String callbackUrl;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TransactionDto transactionDto)) {
            return false;
        }
        return transactionDto.getAmount() == this.getAmount() && transactionDto.getStatus() == this.getStatus()
                && Objects.equals(transactionDto.getCallbackUrl(), this.getCallbackUrl());
    }
}
