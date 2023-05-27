package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacade;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;

/**
 * Interface representing payment facade.
 *
 * @author Juraj Marcin
 */
public interface PaymentFacade extends DomainFacade<PaymentDto, PaymentCreateDto> {

    /**
     * Finalizes a payment by contacting the payment gate to check the status.
     *
     * @param transactionId id of the transaction
     * @return finalized payment
     */
    PaymentDto finalizePayment(String transactionId);
}
