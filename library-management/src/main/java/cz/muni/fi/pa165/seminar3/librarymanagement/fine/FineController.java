package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.BorrowingService;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

/**
 * Fines REST controller
 *
 * @author Juraj Marcin
 */
@RestController
@RequestMapping(path = "/fines")
public class FineController {

    private final FineService fineService;

    private final FineMapper fineMapper;
    private final UserService userService;
    private final BorrowingService borrowingService;

    public FineController(FineService fineService,
                          FineMapper fineMapper,
                          UserService userService,
                          BorrowingService borrowingService) {
        this.fineService = fineService;
        this.fineMapper = fineMapper;
        this.userService = userService;
        this.borrowingService = borrowingService;
    }

    @Operation(summary = "Create a new fine")
    @ApiResponse(responseCode = "200", description = "Fine created", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Issuer or borrowing not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    public FineDto create(@RequestBody FineCreateDto fineCreateDto) {
        try {
            User issuer = userService.find(fineCreateDto.getIssuerId());
            Borrowing outstandingBorrowing = borrowingService.find(fineCreateDto.getOutstandingBorrowingId());
            Fine fine = Fine.builder()
                    .id(UUID.randomUUID().toString())
                    .amount(fineCreateDto.getAmount())
                    .outstandingBorrowing(outstandingBorrowing)
                    .issuer(issuer)
                    .build();
            return fineMapper.toDto(fineService.create(fine));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    @Operation(summary = "List all fines")
    @ApiResponse(responseCode = "200", description = "Pages list of all fines", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid paging",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping
    public Result<FineDto> findAll(Pageable pageable) {
        return fineMapper.toResult(fineService.findAll(pageable));
    }

    @Operation(summary = "Find fine with id")
    @ApiResponse(responseCode = "200", description = "Fine found", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @GetMapping(path = "{id}")
    public FineDto find(@PathVariable String id) {
        try {
            return fineMapper.toDto(fineService.find(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Fine %s not found", id));
        }
    }

    @Operation(summary = "Update fine")
    @ApiResponse(responseCode = "200", description = "Fine updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PutMapping(path = "{id}")
    public FineDto update(@PathVariable String id,
                          @RequestBody FineCreateDto fineCreateDto) {
        try {
            Fine fine = fineService.find(id);
            User issuer = userService.find(fineCreateDto.getIssuerId());
            Borrowing outstandingBorrowing = borrowingService.find(fineCreateDto.getOutstandingBorrowingId());
            fine.setAmount(fineCreateDto.getAmount());
            fine.setIssuer(issuer);
            fine.setOutstandingBorrowing(outstandingBorrowing);
            return fineMapper.toDto(fineService.update(fine));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }

    @Operation(summary = "Delete fine")
    @ApiResponse(responseCode = "200", description = "Fine deleted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Fine not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @DeleteMapping(path = "{id}")
    public void delete(@PathVariable String id) {
        try {
            Fine fine = fineService.find(id);
            fineService.delete(fine);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.toString());
        }
    }
}
