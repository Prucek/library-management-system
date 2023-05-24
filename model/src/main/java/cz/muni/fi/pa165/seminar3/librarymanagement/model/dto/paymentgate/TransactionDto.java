package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


/**
 * DTO for a transaction. Data Transfer Object that is stable for API.
 *
 * @author Peter Rúček
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
}
