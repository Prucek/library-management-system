package cz.muni.fi.pa165.seminar3.paymentgate.transaction;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.CardDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionStatus;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    // singleton
    private TransactionDto transactionDto = null;

    /**
     * Method creating payment => adding ID
     */
    @Operation(
            summary = "Create a new payment",
            responses = {
                    @ApiResponse(responseCode = "201", ref = "#/components/responses/CreatePaymentResponse"),
                    @ApiResponse(responseCode = "400", description = "input data were not correct"),
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto create(@RequestBody TransactionDto dto) {
        transactionDto = dto;
        dto.setId(UUID.randomUUID().toString());
        dto.setCallbackURL("/payment/" + dto.getId());
        return dto;
    }

    /**
     * Method returning the payment
     */
    @Operation(
            summary = "Returns created payment",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "#/components/responses/FindPaymentResponse"),
                    @ApiResponse(responseCode = "404", description = "message not found")
            }
    )
    @GetMapping("/{id}")
    public TransactionDto find(@PathVariable String id) {
        if (Objects.equals(transactionDto.getId(), id)){
            return transactionDto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "payment with id=" + id + " not found");
        }
    }

    /**
     * Method simulating the pay of the payment
     */
    @Operation(
            summary = "Pay created payment",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "#/components/responses/PayPaymentResponse"),
                    @ApiResponse(responseCode = "400", description = "input data were not correct"),
            }
    )
    @PostMapping("/{id}")
    public TransactionDto pay(@PathVariable String id, @RequestBody CardDto cardDto) {
        if (!Objects.equals(transactionDto.getId(), id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "payment with id=" + id + " not found");
        }

        if (cardDto.getCardNumber() != null && cardDto.getExpiration() != null && cardDto.getCvv2() != null){
            transactionDto.setStatus(TransactionStatus.APPROVED);
        } else {
            transactionDto.setStatus(TransactionStatus.DECLINED);
        }
        return transactionDto;
    }
}
