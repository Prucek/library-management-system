package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.FineService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

/**
 * Payments REST Controller
 */
@RestController
@RequestMapping(path = "/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final FineService fineService;
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentController(PaymentService paymentService,
                             FineService fineService,
                             PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.fineService = fineService;
        this.paymentMapper = paymentMapper;
    }

    @Operation(summary = "Create a new payment for fines")
    @ApiResponse(responseCode = "200", description = "Payment created, proceed to payment gate",
            useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    public PaymentDto create(@RequestBody PaymentCreateDto paymentCreateDto) {
        try {
            String newTransactionId = UUID.randomUUID().toString(); // from payment gate microservice
            return paymentMapper.toDto(paymentService.create(Payment.builder()
                    .id(UUID.randomUUID().toString())
                    .status(PaymentStatus.CREATED)
                    .transactionId(newTransactionId)
                    .paidFines(paymentCreateDto.getFines().stream().map(fineService::find).toList())
                    .build()));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    @Operation(summary = "Callback for the payment gate",
            description = "Checks the transaction status with the payment gate and updates the status")
    @ApiResponse(responseCode = "200", description = "Payment status updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Payment not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping(path = "{id}")
    public PaymentDto paymentGateCallback(@PathVariable String id) {
        try {
            Payment payment = paymentService.find(id);
            // contact payment gate and check if the transaction is accepted
            payment.setStatus(PaymentStatus.PAID);
            paymentService.update(payment);
            return paymentMapper.toDto(payment);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    @Operation(summary = "List all payments")
    @ApiResponse(responseCode = "200", description = "Paged list of payments", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<PaymentDto> findAll(Pageable pageable) {
        return paymentMapper.toResult(paymentService.findAll(pageable));
    }

    @Operation(summary = "Find a payment")
    @ApiResponse(responseCode = "200", description = "Payment found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Payment not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping(path = "{id}")
    public PaymentDto find(@PathVariable String id) {
        try {
            return paymentMapper.toDto(paymentService.find(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }
}
