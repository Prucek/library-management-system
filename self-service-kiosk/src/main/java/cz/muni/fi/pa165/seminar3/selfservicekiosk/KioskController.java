package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Author.AuthorService;
import cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Book.BookService;
import cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.BookInstance.BookInstance;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Book.BookInstanceDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskReturnDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
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

    @PostMapping("/borrow")
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

    @PostMapping("/return")
    public BorrowingDto return_book(@RequestBody KioskReturnDto dto){
        System.out.println(dto.getBookInstanceId());
        BorrowingDto newBorrowing = new BorrowingDto();
//      Todo:  Find book instance by its id

        newBorrowing.setBookInstance(new BookInstanceDto());
        newBorrowing.setUser(new UserDto());
        newBorrowing.setFrom(LocalDateTime.now());
        newBorrowing.setTo(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
        return newBorrowing;
    }
}
