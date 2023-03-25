package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for a payment. Data Transfer Object that is stable for API.
 */
@Getter
@Setter
public class PaymentDto extends DomainObjectDto {
    private double amount;
    private PaymentStatus status = PaymentStatus.WAITING;
    private String callbackURL;
}
