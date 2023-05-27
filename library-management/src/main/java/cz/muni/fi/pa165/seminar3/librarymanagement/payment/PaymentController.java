package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import static cz.muni.fi.pa165.seminar3.librarymanagement.Config.DEFAULT_PAGE_SIZE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_OAUTH2;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.USER_SCOPE;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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

/**
 * Class representing payment controller.
 *
 * @author Juraj Marcin
 */
@RestController
@RequestMapping(path = "/payments")
@ApiResponse(responseCode = "401", description = "Authentication is required")
@ApiResponse(responseCode = "403", description = "Access token does not have required scope")
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
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "201", description = "Payment created, proceed to payment gate",
            useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto create(@RequestBody @Valid PaymentCreateDto paymentCreateDto) {
        return paymentFacade.create(paymentCreateDto);
    }

    /**
     * Updates the payment status by contacting the payment gate.
     *
     * @param transactionId id of the transaction
     * @return updated payment
     */
    @Operation(summary = "Callback for the payment gate",
            description = "Checks the transaction status with the payment gate and updates the status")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "200", description = "Payment status updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Payment not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping(path = "{transactionId}")
    public PaymentDto paymentGateCallback(@PathVariable String transactionId) {
        return paymentFacade.finalizePayment(transactionId);
    }

    /**
     * Finds all payments.
     *
     * @param page     page number
     * @param pageSize size of the page
     * @return paged payments
     */
    @Operation(summary = "List all payments")
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
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
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {USER_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {USER_SCOPE})
    @ApiResponse(responseCode = "200", description = "Payment found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Payment not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping(path = "{id}")
    public PaymentDto find(@PathVariable String id) {
        return paymentFacade.find(id);
    }
}
