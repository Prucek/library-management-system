package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Class representing payment controller
 *
 * @author Juraj Marcin
 */
@RestController
@RequestMapping(path = "/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    /**
     * Create a new payment controller instance
     *
     * @param paymentFacade payment facade instance
     */
    @Autowired
    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
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
            return paymentFacade.create(paymentCreateDto);
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
            return paymentFacade.finalizePayment(id);
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
        return paymentFacade.findAll(pageable);
    }

    @Operation(summary = "Find a payment")
    @ApiResponse(responseCode = "200", description = "Payment found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Payment not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping(path = "{id}")
    public PaymentDto find(@PathVariable String id) {
        try {
            return paymentFacade.find(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }
}
