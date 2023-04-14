package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskReturnDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

/**
 * REST API for actions around kiosk.
 */

@RestController
@OpenAPIDefinition(
        info = @Info(title = "RestAPI controller for borrowing and returning at kiosk",
                version = "1.0",
                contact = @Contact(name = "Marek Fiala", email = "xfiala6@fi.muni.cz")
        )
)
@RequestMapping("/kiosk")
public class KioskController {

//    Example dummy data for testing:
    String exampleBookInstanceId = "123";
    String exampleUserId = "456";

    /**
     * User can borrow a book at kiosk
     */
    @Operation(summary = "Borrow a book at kiosk")
    @ApiResponse(responseCode = "202", description = "New borrowing accepted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid Payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping("/borrow")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BorrowingDto borrow(@RequestBody KioskBorrowDto dto){
        if(!Objects.equals(dto.getBookInstanceId(), exampleBookInstanceId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book instance with id:" + dto.getBookInstanceId() + " not found");
        }
        if (!Objects.equals(dto.getUserId(), exampleUserId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id:" + dto.getUserId() + " not found");
        }
//      Todo:  Find book instance by its id
//      Todo:  Find user by its id

        BorrowingDto newBorrowing = new BorrowingDto();
        newBorrowing.setId(UUID.randomUUID().toString());
        newBorrowing.setBookInstance(new BookInstanceDto());
        newBorrowing.setUser(new UserDto());
        newBorrowing.setFrom(LocalDateTime.now());
        newBorrowing.setTo(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
        return newBorrowing;
    }

    /**
     * User can return a book at kiosk
     */
    @Operation(summary = "Return a book at kiosk")
    @ApiResponse(responseCode = "202", description = "New borrowing accepted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid Payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BorrowingDto returnBook(@RequestBody KioskReturnDto dto){
        if(!Objects.equals(dto.getBookInstanceId(), exampleBookInstanceId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book instance with id:" + dto.getBookInstanceId() + " not found");
        }
//      Todo:  Find book instance by its id

        BorrowingDto newBorrowing = new BorrowingDto();
        newBorrowing.setId(UUID.randomUUID().toString());
        newBorrowing.setBookInstance(new BookInstanceDto());
        newBorrowing.setUser(new UserDto());
        newBorrowing.setFrom(LocalDateTime.now());
        newBorrowing.setTo(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
        return newBorrowing;
    }
}
