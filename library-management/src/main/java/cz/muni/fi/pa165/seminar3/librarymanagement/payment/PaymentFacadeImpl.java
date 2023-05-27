package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.Fine;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.FineService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentStatus;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Class representing payment facade implementation.
 *
 * @author Juraj Marcin
 */
@Service
public class PaymentFacadeImpl extends DomainFacadeImpl<Payment, PaymentDto, PaymentCreateDto>
        implements PaymentFacade {

    @Value("${payment-gate.callback}")
    @NotBlank
    private String paymentGateCallback;

    @Getter
    private final PaymentService domainService;

    @Getter
    private final PaymentMapper domainMapper;
    private final FineService fineService;
    private final PaymentGateApi paymentGateApi;

    /**
     * Creates a new payment facade.
     *
     * @param domainService  payment service instance
     * @param domainMapper   payment mapper instance
     * @param fineService    fine service instance
     * @param paymentGateApi payment gate API client
     */
    @Autowired
    public PaymentFacadeImpl(PaymentService domainService, PaymentMapper domainMapper, FineService fineService,
                             PaymentGateApi paymentGateApi) {

        this.domainService = domainService;
        this.domainMapper = domainMapper;
        this.fineService = fineService;
        this.paymentGateApi = paymentGateApi;
    }

    @Override
    public PaymentDto create(PaymentCreateDto paymentCreateDto) {
        List<Fine> fines = paymentCreateDto.getFines().stream().map(fineService::find).toList();
        TransactionDto transaction = paymentGateApi.createTransaction(fines.stream().mapToDouble(Fine::getAmount).sum(),
                paymentGateCallback);
        Payment payment = domainService.create(Payment.builder()
                .status(PaymentStatus.CREATED)
                .transactionId(transaction.getId())
                .paidFines(fines)
                .build());
        fines.forEach(fine -> fine.setResolvingPayment(payment));
        fines.forEach(fineService::update);
        return domainMapper.toDto(payment);
    }

    @Override
    public PaymentDto finalizePayment(String transactionId) {
        Payment payment = domainService.findByTransactionId(transactionId);
        TransactionDto transaction = paymentGateApi.findTransaction(payment.getTransactionId());
        switch (transaction.getStatus()) {
            case APPROVED -> payment.setStatus(PaymentStatus.PAID);
            case DECLINED -> payment.setStatus(PaymentStatus.CANCELED);
            default -> payment.setStatus(PaymentStatus.CREATED);
        }
        domainService.update(payment);
        return domainMapper.toDto(payment);
    }
}
