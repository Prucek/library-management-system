package cz.muni.fi.pa165.seminar3.librarymanagement;

import cz.muni.fi.pa165.seminar3.librarymanagement.address.Address;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.AuthorService;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.Book;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookInstance;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookService;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.BorrowingService;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.Fine;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.FineService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentStatus;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import cz.muni.fi.pa165.seminar3.librarymanagement.payment.Payment;
import cz.muni.fi.pa165.seminar3.librarymanagement.payment.PaymentService;
import cz.muni.fi.pa165.seminar3.librarymanagement.reservation.Reservation;
import cz.muni.fi.pa165.seminar3.librarymanagement.reservation.ReservationService;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


/**
 * Class for initializing data in database.
 */
@Profile("!test")
@Component
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;

    private final ReservationService reservationService;

    private final BorrowingService borrowingService;

    private final FineService fineService;

    private final PaymentService paymentService;

    private final BookService bookService;

    private final AuthorService authorService;

    /**
     * Constructor for all used services.
     *
     * @param userService        user service instance
     * @param reservationService reservation service instance
     * @param borrowingService   borrowing service instance
     * @param fineService        fine service instance
     * @param paymentService     payment service instance
     * @param bookService        book service instance
     * @param authorService      author service instance
     */
    public DataInitializer(UserService userService, ReservationService reservationService,
                           BorrowingService borrowingService, FineService fineService, PaymentService paymentService,
                           BookService bookService, AuthorService authorService) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.borrowingService = borrowingService;
        this.fineService = fineService;
        this.paymentService = paymentService;
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @Override
    public void run(ApplicationArguments args) {

        Address address = Address.builder().city("Brno").country("CZ").street("Hrnčírska").houseNumber("99").build();


        User user = User.builder()
                .email("test@email.com")
                .firstName("John")
                .lastName("Doe")
                .username("johnD")
                .password("password")
                .userType(UserType.CLIENT)
                .addresses(List.of(address))
                .build();

        userService.create(user);

        Author author = Author.builder().name("John").surname("Wick").build();
        Author author2 = Author.builder().name("Stephan").surname("Hawking").build();
        Author author3 = Author.builder().name("Janis").surname("Smith").build();
        Author author4 = Author.builder().name("Alois").surname("Jirasek").build();

        authorService.create(author);
        authorService.create(author2);
        authorService.create(author3);
        authorService.create(author4);

        Book book1 = Book.builder().title("Sloni žerou medvědy").author(author).author(author2).build();
        Book book2 = Book.builder().title("Povidky Malostranske").author(author3).build();
        Book book3 = Book.builder().title("Outcast").author(author4).build();

        bookService.create(book1);
        bookService.create(book2);
        bookService.create(book3);

        BookInstance book1instance = bookService.addInstance(book1.getId());
        bookService.addInstance(book1.getId());
        bookService.addInstance(book2.getId());
        bookService.addInstance(book2.getId());
        bookService.addInstance(book3.getId());

        Borrowing borrowing = Borrowing.builder()
                .from(LocalDateTime.now().minusDays(40))
                .to(LocalDateTime.now().minusDays(10))
                .returned(LocalDateTime.now())
                .user(user)
                .bookInstance(book1instance)
                .build();

        borrowingService.create(borrowing);

        Reservation reservation = Reservation.builder()
                .from(LocalDateTime.now())
                .to(LocalDateTime.now().plus(10, ChronoUnit.DAYS))
                .user(user)
                .book(book2)
                .build();

        reservationService.create(reservation);

        Fine fine = Fine.builder().issuer(user).outstandingBorrowing(borrowing).amount(42.0).build();

        fineService.create(fine);

        Payment payment = Payment.builder()
                .transactionId(UUID.randomUUID().toString())
                .status(PaymentStatus.PAID)
                .paidFines(List.of(fine))
                .build();

        paymentService.create(payment);

        Author author = Author.builder().name("John").surname("Wick").build();

        authorService.create(author);

        Author author2 = Author.builder().name("Stephan").surname("Hawking").build();

        authorService.create(author2);

        Book book = Book.builder().title("Sloni žerou medvědy").authors(List.of(author, author2)).build();

        bookService.create(book);

        bookService.addInstance(book.getId());
        bookService.addInstance(book.getId());
    }
}
