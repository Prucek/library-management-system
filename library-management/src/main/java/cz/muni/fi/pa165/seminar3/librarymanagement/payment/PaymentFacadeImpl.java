package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.FineService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentStatus;
import java.util.UUID;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class PaymentFacadeImpl extends DomainFacadeImpl<Payment, PaymentDto, PaymentCreateDto>
        implements PaymentFacade {

    @Getter
    private final PaymentService domainService;

    @Getter
    private final PaymentMapper domainMapper;
    private final FineService fineService;

    public PaymentFacadeImpl(PaymentService domainService, PaymentMapper domainMapper, FineService fineService) {

        this.domainService = domainService;
        this.domainMapper = domainMapper;
        this.fineService = fineService;
    }

    @Override
    public PaymentDto create(PaymentCreateDto paymentCreateDto) {
        String newTransactionId = UUID.randomUUID().toString(); // from payment gate microservice
        return domainMapper.toDto(domainService.create(Payment.builder()
                .id(UUID.randomUUID().toString())
                .status(PaymentStatus.CREATED)
                .transactionId(newTransactionId)
                .paidFines(paymentCreateDto.getFines().stream().map(fineService::find).toList())
                .build()));
    }

    @Override
    public PaymentDto finalizePayment(String id) {
        Payment payment = domainService.find(id);
        // contact payment gate and check if the transaction is accepted
        payment.setStatus(PaymentStatus.PAID);
        domainService.update(payment);
        return domainMapper.toDto(payment);
    }
}
