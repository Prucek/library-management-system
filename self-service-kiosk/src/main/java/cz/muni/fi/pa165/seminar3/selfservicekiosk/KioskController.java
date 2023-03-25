package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Author.AuthorService;
import cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Book.BookService;
import cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.BookInstance.BookInstance;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book.BookInstanceDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskReturnDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

//    For later
//    private final AuthorService authroService;
//    private final BookService bookService;
//
//    public KioskController(AuthorService authroService, BookService bookService) {
//        this.authroService = authroService;
//        this.bookService = bookService;
//    }


    /**
     * User can borrow a book at kiosk
     */
    @Operation(
            summary = "Borrow a book at kiosk",
            description = "New borrowing is made based on incoming BookInstanceId and USerId data",
            responses = {
                    @ApiResponse(responseCode = "202", ref = "#/components/responses/KioskBorrowResponse")
//                    Todo: response
//                    @ApiResponse(responseCode = "400", description = "input data were not correct"),
            }
    )
    @PostMapping("/borrow")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BorrowingDto borrow(@RequestBody KioskBorrowDto dto){
        BorrowingDto newBorrowing = new BorrowingDto();
//      Todo:  Find book instance by its id
//      Todo:  Find user by its id

        newBorrowing.setBookInstance(new BookInstanceDto());
        newBorrowing.setUser(new UserDto());
        newBorrowing.setFrom(LocalDateTime.now());
        newBorrowing.setTo(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
        return newBorrowing;
    }

    /**
     * User can return a book at kiosk
     */
    @Operation(
            summary = "Return a book at kiosk",
            description = "Returning is made based on incoming BookInstanceId data",
            responses = {
                    @ApiResponse(responseCode = "202", ref = "#/components/responses/KioskReturnResponse")
//                    Todo: response
//                    @ApiResponse(responseCode = "400", description = "input data were not correct"),
            }
    )
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BorrowingDto return_book(@RequestBody KioskReturnDto dto){
        BorrowingDto newBorrowing = new BorrowingDto();
//      Todo:  Find book instance by its id

        newBorrowing.setBookInstance(new BookInstanceDto());
        newBorrowing.setUser(new UserDto());
        newBorrowing.setFrom(LocalDateTime.now());
        newBorrowing.setTo(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
        return newBorrowing;
    }
}
