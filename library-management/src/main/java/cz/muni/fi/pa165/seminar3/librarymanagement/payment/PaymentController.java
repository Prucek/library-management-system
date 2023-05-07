package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import static cz.muni.fi.pa165.seminar3.librarymanagement.Config.DEFAULT_PAGE_SIZE;

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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Class representing payment controller.
 *
 * @author Juraj Marcin
 */
@RestController
@RequestMapping(path = "/payments")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    /**
     * Create a new payment controller instance.
     *
     * @param paymentFacade payment facade instance
     */
    @Autowired
    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    /**
     * Creates a new payment.
     *
     * @param paymentCreateDto payment creation data
     * @return created payment
     */
    @Operation(summary = "Create a new payment for fines")
    @ApiResponse(responseCode = "200", description = "Payment created, proceed to payment gate",
            useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto create(@RequestBody PaymentCreateDto paymentCreateDto) {
        try {
            return paymentFacade.create(paymentCreateDto);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    /**
     * Updates the payment status by contacting the payment gate.
     *
     * @param id id of the payment
     * @return updated payment
     */
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

    /**
     * Finds all payments.
     *
     * @param page     page number
     * @param pageSize size of the page
     * @return paged payments
     */
    @Operation(summary = "List all payments")
    @ApiResponse(responseCode = "200", description = "Paged list of payments", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<PaymentDto> findAll(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return paymentFacade.findAll(page, pageSize);
    }

    /**
     * Finds a payment with id.
     *
     * @param id id of the payment
     * @return found payment
     */
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
