package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API for actions around kiosk.
 *
 * @author Marek Fiala
 */

@RestController
@OpenAPIDefinition(info = @Info(title = "RestAPI controller for borrowing and returning at kiosk", version = "1.0",
        contact = @Contact(name = "Marek Fiala", email = "xfiala6@fi.muni.cz")))
@RequestMapping("/kiosk")
public class KioskController {

    private final KioskFacade kioskFacade;

    @Autowired
    public KioskController(KioskFacade kioskFacade) {
        this.kioskFacade = kioskFacade;
    }

    /**
     * User can borrow a book at kiosk.
     */
    @Operation(summary = "Borrow a book at kiosk")
    @ApiResponse(responseCode = "202", description = "New borrowing accepted", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid Payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping("/borrow")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BorrowingDto borrow(@RequestBody KioskBorrowDto dto) {
        return kioskFacade.borrowBook(dto);
    }

    /**
     * User can return a book at kiosk.
     */
    @Operation(summary = "Return a book at kiosk")
    @ApiResponse(responseCode = "200", description = "Returned (borrowing deleted)", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "404", description = "Borrowing not found",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping("/return/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void returnBook(@PathVariable String id) {
        kioskFacade.returnBook(id);
    }
}
