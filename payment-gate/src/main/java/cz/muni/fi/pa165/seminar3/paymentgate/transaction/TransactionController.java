package cz.muni.fi.pa165.seminar3.paymentgate.transaction;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.CardDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
 * Controller for creating transactions and payments.
 *
 * @author Peter Rúček
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService service;


    public TransactionController(TransactionService transactionService) {
        this.service = transactionService;
    }

    /**
     * Method creating payment => adding ID.
     */
    @Operation(summary = "Create a new payment")
    @ApiResponse(responseCode = "201", description = "Payment created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto create(@RequestBody @Valid TransactionCreateDto dto) {

        if (dto.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount must be positive");
        }
        return service.create(dto);
    }

    /**
     * Method returning all payments.
     */
    @Operation(summary = "List all payments")
    @ApiResponse(responseCode = "200", description = "Pages list of all payments", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<TransactionDto> findAll(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "50") int pageSize) {
        return service.findAll(page, pageSize);
    }

    /**
     * Method returning the payment.
     */
    @Operation(summary = "Returns created payment")
    @ApiResponse(responseCode = "200", description = "Payment found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Payment not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping("/{id}")
    public TransactionDto find(@PathVariable String id) {
        return service.find(id);
    }

    /**
     * Method simulating the pay of the payment.
     */
    @Operation(summary = "Pays for payment of {id} and returns")
    @ApiResponse(responseCode = "200", description = "Payment paid", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Payment not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping("/{id}")
    public TransactionDto pay(@PathVariable String id, @RequestBody @Valid CardDto cardDto) {
        return service.pay(id, cardDto);
    }
}