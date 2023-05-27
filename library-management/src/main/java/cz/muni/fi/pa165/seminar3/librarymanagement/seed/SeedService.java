package cz.muni.fi.pa165.seminar3.librarymanagement.seed;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.AuthorRepository;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.Book;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookInstance;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookInstanceRepository;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookRepository;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.BorrowingRepository;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.Fine;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.FineRepository;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import cz.muni.fi.pa165.seminar3.librarymanagement.settings.Settings;
import cz.muni.fi.pa165.seminar3.librarymanagement.settings.SettingsService;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

/**
 * Class representing seed service.
 *
 * @author Juraj Marcin
 */
@Service
public class SeedService {

    private static final int AUTHORS = 20;
    private static final int BOOKS_PER_AUTHOR_MIN = 2;
    private static final int BOOKS_PER_AUTHOR_MAX = 5;
    private static final int INSTANCES_PER_BOOK_MIN = 1;
    private static final int INSTANCES_PER_BOOK_MAX = 6;
    private static final int LIBRARIANS = 5;
    private static final int CLIENTS = 100;
    private static final int BORROWINGS_PER_USER_MIN = 0;
    private static final int BORROWINGS_PER_USER_MAX = 10;


    private final SettingsService settingsService;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookInstanceRepository bookInstanceRepository;
    private final UserRepository userRepository;
    private final BorrowingRepository borrowingsRepository;
    private final FineRepository fineRepository;

    /**
     * Creates a new seed service instance.
     *
     * @param authorRepository       author repository instance
     * @param bookRepository         book repository instance
     * @param bookInstanceRepository book instance repository instance
     * @param userRepository         user repository instance
     * @param settingsService        settings service instance
     * @param fineRepository         fine repository instance
     * @param borrowingsRepository   borrowings repository instance
     */
    public SeedService(SettingsService settingsService, AuthorRepository authorRepository,
                       BookRepository bookRepository, BookInstanceRepository bookInstanceRepository,
                       UserRepository userRepository, BorrowingRepository borrowingsRepository,
                       FineRepository fineRepository) {
        this.settingsService = settingsService;
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.bookInstanceRepository = bookInstanceRepository;
        this.userRepository = userRepository;
        this.borrowingsRepository = borrowingsRepository;
        this.fineRepository = fineRepository;
    }

    private List<Author> seedAuthors(Faker faker) {
        List<Author> authors = Stream.iterate(0, i -> i + 1)
                .limit(AUTHORS)
                .map(i -> (Author) Author.builder()
                        .name(faker.name().firstName())
                        .surname(faker.name().lastName())
                        .build())
                .toList();
        return authorRepository.saveAll(authors);
    }

    private List<Book> seedBooksToAuthors(Faker faker, List<Author> authors) {
        List<Book> books = authors.stream()
                .flatMap(author -> Stream.iterate(0, i -> i + 1)
                        .limit(faker.random().nextInt(BOOKS_PER_AUTHOR_MIN, BOOKS_PER_AUTHOR_MAX))
                        .map(i -> (Book) Book.builder().title(faker.book().title()).authors(List.of(author)).build()))
                .toList();
        return bookRepository.saveAll(books);
    }

    private List<BookInstance> seedInstancesToBooks(Faker faker, List<Book> books) {
        List<BookInstance> bookInstances = books.stream()
                .flatMap(book -> Stream.iterate(0, i -> i + 1)
                        .limit(faker.random().nextInt(INSTANCES_PER_BOOK_MIN, INSTANCES_PER_BOOK_MAX))
                        .map(i -> (BookInstance) BookInstance.builder().book(book).build()))
                .toList();
        return bookInstanceRepository.saveAll(bookInstances);
    }

    private List<Address> getAddresses(Faker faker, long count) {
        return Stream.iterate(0, i -> i + 1).limit(count).map(i -> faker.address()).toList();
    }

    private List<User> seedLibrarians(Faker faker) {
        List<Address> addresses = getAddresses(faker, LIBRARIANS);
        List<User> librarians = Stream.iterate(0, i -> i + 1)
                .limit(LIBRARIANS)
                .map(i -> (User) User.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .username(faker.name().username())
                        .email(faker.internet().emailAddress())
                        .userType(UserType.LIBRARIAN)
                        .street(addresses.get(i).streetName())
                        .houseNumber(addresses.get(i).streetAddressNumber())
                        .city(addresses.get(i).city())
                        .zip(addresses.get(i).zipCode())
                        .country(addresses.get(i).country())
                        .build())
                .toList();
        return userRepository.saveAll(librarians);
    }

    private List<User> seedClients(Faker faker) {
        List<Address> addresses = getAddresses(faker, CLIENTS);
        List<User> librarians = Stream.iterate(0, i -> i + 1)
                .limit(CLIENTS)
                .map(i -> (User) User.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .username(faker.name().username())
                        .email(faker.internet().emailAddress())
                        .userType(UserType.CLIENT)
                        .street(addresses.get(i).streetName())
                        .houseNumber(addresses.get(i).streetAddressNumber())
                        .city(addresses.get(i).city())
                        .zip(addresses.get(i).zipCode())
                        .country(addresses.get(i).country())
                        .build())
                .toList();
        return userRepository.saveAll(librarians);
    }

    private List<Borrowing> seedBorrowingsToClients(Faker faker, List<User> clients, List<BookInstance> bookInstances,
                                                    Settings settings) {
        Map<String, Date> bookInstanceFree = new HashMap<>();
        List<Borrowing> borrowings = clients.stream()
                .flatMap(user -> Stream.iterate(0, i -> i + 1)
                        .limit(faker.random().nextInt(BORROWINGS_PER_USER_MIN, BORROWINGS_PER_USER_MAX))
                        .map(i -> {
                            BookInstance bookInstance =
                                    bookInstances.get(faker.random().nextInt(0, bookInstances.size() - 1));
                            LocalDateTime to;
                            LocalDateTime returned = null;
                            if (bookInstanceFree.containsKey(bookInstance.getId())) {
                                to = faker.date()
                                        .past(14, TimeUnit.DAYS, bookInstanceFree.get(bookInstance.getId()))
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime();
                                returned = faker.date()
                                        .past(settings.getBorrowingLimit(), TimeUnit.DAYS,
                                                bookInstanceFree.get(bookInstance.getId()))
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime();
                            } else {
                                to = faker.date()
                                        .future(15, TimeUnit.DAYS)
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDateTime();
                            }
                            LocalDateTime from = to.minusDays(settings.getBorrowingLimit());
                            bookInstanceFree.put(bookInstance.getId(),
                                    Date.from(from.minusDays(1).toInstant(ZoneOffset.UTC)));
                            return (Borrowing) Borrowing.builder()
                                    .user(user)
                                    .bookInstance(bookInstance)
                                    .borrowedFrom(from)
                                    .borrowedTo(to)
                                    .returned(returned)
                                    .build();
                        }))
                .toList();
        return borrowingsRepository.saveAll(borrowings);
    }

    private List<Fine> seedFinesToBorrowings(Faker faker, List<Borrowing> borrowings, List<User> librarians,
                                             Settings settings) {
        List<Fine> fines = borrowings.stream()
                .filter(borrowing -> borrowing.getReturned() != null && borrowing.getReturned()
                        .toLocalDate()
                        .isAfter(borrowing.getBorrowedTo().toLocalDate()))
                .map(borrowing -> (Fine) Fine.builder()
                        .outstandingBorrowing(borrowing)
                        .issuer(librarians.get(faker.random().nextInt(0, librarians.size() - 1)))
                        .amount(Duration.between(borrowing.getBorrowedTo().toLocalDate().atStartOfDay(),
                                borrowing.getReturned().toLocalDate().atStartOfDay()).toDays()
                                * settings.getFinePerDay())
                        .build())
                .toList();
        return fineRepository.saveAll(fines);
    }

    /**
     * Seeds the database.
     */
    public void seed() {
        Faker faker = new Faker();
        Settings settings = settingsService.getCurrent();

        List<Author> authors = seedAuthors(faker);
        List<Book> books = seedBooksToAuthors(faker, authors);
        List<BookInstance> bookInstances = seedInstancesToBooks(faker, books);
        List<User> librarians = seedLibrarians(faker);
        List<User> clients = seedClients(faker);
        List<Borrowing> borrowings = seedBorrowingsToClients(faker, clients, bookInstances, settings);
        List<Fine> fines = seedFinesToBorrowings(faker, borrowings, librarians, settings);
    }

    /**
     * Drops the database.
     */
    public void drop() {
        fineRepository.deleteAll();
        borrowingsRepository.deleteAll();
        userRepository.deleteAll();
        bookInstanceRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }
}
