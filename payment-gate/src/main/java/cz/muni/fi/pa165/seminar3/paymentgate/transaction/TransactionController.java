package cz.muni.fi.pa165.seminar3.paymentgate.transaction;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.CardDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


/**
 * Controller for creating transactions and payments.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    private final TransactionMapper mapper;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.service = transactionService;
        this.mapper = transactionMapper;
    }

    /**
     * Method creating payment => adding ID.
     */
    @Operation(summary = "Create a new payment")
    @ApiResponse(responseCode = "200", description = "Payment created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto create(@RequestBody TransactionCreateDto dto) {

        if (dto.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount must be positive");
        }
        Transaction transaction =  service.create(mapper.fromCreateDto(dto));
        transaction.setStatus(TransactionStatus.WAITING);
        transaction.setCallbackUrl("/transactions/" + transaction.getId());
        return mapper.toDto(transaction);
    }

    /**
     * Method returning all payments.
     */
    @Operation(summary = "List all payments")
    @ApiResponse(responseCode = "200", description = "Pages list of all payments", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<TransactionDto> findAll(Pageable pageable) {
        return mapper.toResult(service.findAll(pageable));
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

        Transaction transaction;
        try {
            transaction = service.find(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "transaction with id=" + id + " not found");
        }
        return mapper.toDto(transaction);
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
    public TransactionDto pay(@PathVariable String id, @RequestBody CardDto cardDto) {

        // To check error
        find(id);
        Transaction transaction = service.find(id);

        if (transaction.getStatus() != TransactionStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transaction with id=" + id + " is not waiting");
        }

        if (cardDto.getCardNumber() != null && cardDto.getExpiration() != null && cardDto.getCvv2() != null) {
            transaction.setStatus(TransactionStatus.APPROVED);
        } else {
            transaction.setStatus(TransactionStatus.DECLINED);
        }

        return mapper.toDto(service.update(transaction));
    }
}
