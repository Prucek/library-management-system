package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBookInstance;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BorrowingUtils.fakeBorrowing;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.UserUtils.fakeUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookInstance;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests for borrowing facade.
 *
 * @author Marek Fiala
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class BorrowingFacadeImplTest {

    @Autowired
    private BorrowingRepository domainRepository;

    @Autowired
    private BorrowingMapper domainMapper;

    @Autowired
    private BorrowingFacade borrowingFacade;

    @MockBean
    private UserService userService;

    @MockBean
    private BookService bookService;

    private final Faker faker = new Faker();

    @Test
    public void createBorrowingSuccessful() {
        User fakeUser = fakeUser(faker, UserType.CLIENT);
        BookInstance fakeBookInsance = fakeBookInstance(faker);
        Borrowing fakeBorrowing = fakeBorrowing(faker);
        fakeBorrowing.setBookInstance(fakeBookInsance);
        fakeBorrowing.setUser(fakeUser);

        BorrowingCreateDto borrowingCreateDto = BorrowingCreateDto.builder()
                .to(fakeBorrowing.getTo())
                .from(fakeBorrowing.getFrom())
                .returned(fakeBorrowing.getReturned())
                .userId(fakeBorrowing.getUser().getId())
                .bookInstanceId(fakeBorrowing.getBookInstance().getId())
                .build();

        given(userService.find(any(String.class))).willReturn(fakeUser);
        given(bookService.getInstance(any(String.class))).willReturn(fakeBookInsance);

        Borrowing borrowingResult = domainMapper.fromDto(borrowingFacade.create(borrowingCreateDto));
        assertThat(domainRepository.findById(borrowingResult.getId())).isPresent();
        assertThat(borrowingResult).isEqualTo(fakeBorrowing);
    }

    @Test
    public void createBorrowingEmptyCreateDto() {
        Borrowing fakeBorrowing = Borrowing.builder().build();
        BorrowingCreateDto borrowingCreateDto = BorrowingCreateDto.builder().build();

        given(userService.find(any(String.class))).willReturn(null);
        given(bookService.getInstance(any(String.class))).willReturn(null);

        Borrowing borrowingResult = domainMapper.fromDto(borrowingFacade.create(borrowingCreateDto));
        assertThat(domainRepository.findById(borrowingResult.getId())).isPresent();
        assertThat(borrowingResult).isEqualTo(fakeBorrowing);
    }

    @Test
    public void createBorrowingNullCreateDto() {
        BorrowingCreateDto borrowingCreateDto = null;
        assertThrows(NullPointerException.class, () -> borrowingFacade.create(borrowingCreateDto));
    }

    @Test
    @Transactional
    public void updateBorrowingSuccessful() {
        Borrowing borrowing = createBorrowing();
        BorrowingCreateDto updatedBorrowingCreateDto = BorrowingCreateDto.builder().to(LocalDateTime.now()).build();

        given(userService.find(any(String.class))).willReturn(borrowing.getUser());
        given(bookService.getInstance(any(String.class))).willReturn(borrowing.getBookInstance());

        BorrowingDto updatedBorrowingDto =
                borrowingFacade.updateBorrowing(borrowing.getId(), updatedBorrowingCreateDto);
        assertThat(domainRepository.findById(updatedBorrowingDto.getId())).isPresent();
        assertThat(updatedBorrowingCreateDto.getTo()).isEqualTo(updatedBorrowingDto.getTo());
    }

    @Test
    @Transactional
    public void updateBorrowingEmptyCreateDto() {
        Borrowing borrowing = createBorrowing();
        BorrowingCreateDto emptyBorrowingCreateDto = BorrowingCreateDto.builder().build();
        Borrowing emptyBorrowing = Borrowing.builder().build();

        given(userService.find(any(String.class))).willReturn(null);
        given(bookService.getInstance(any(String.class))).willReturn(null);

        BorrowingDto updatedBorrowingDto = borrowingFacade.updateBorrowing(borrowing.getId(), emptyBorrowingCreateDto);
        assertThat(domainRepository.findById(updatedBorrowingDto.getId())).isPresent();
        assertThat(domainMapper.fromDto(updatedBorrowingDto)).isEqualTo(emptyBorrowing);
    }

    @Test
    public void updateBorrowingNullId() {
        BorrowingCreateDto borrowingCreateDto = BorrowingCreateDto.builder().build();
        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> borrowingFacade.updateBorrowing(null, borrowingCreateDto));
    }

    @Test
    public void updateBorrowingNotFound() {
        Borrowing borrowing = createBorrowing();
        BorrowingCreateDto borrowingCreateDto = null;

        assertThrows(NullPointerException.class,
                () -> borrowingFacade.updateBorrowing(borrowing.getId(), borrowingCreateDto));
    }

    @Test
    public void deleteBorrowingSuccessful() {
        Borrowing borrowing = createBorrowing();

        borrowingFacade.deleteBorrowing(borrowing.getId());
        assertThat(domainRepository.findById(borrowing.getId())).isEmpty();
        assertThat(userService.find(borrowing.getUser().getId())).isNotNull();
        assertThat(bookService.getInstance(borrowing.getBookInstance().getId())).isNotNull();
    }

    @Test
    public void deleteBorrowingNullId() {
        assertThrows(InvalidDataAccessApiUsageException.class, () -> borrowingFacade.deleteBorrowing(null));
    }

    @Test
    public void deleteBorrowingNotFound() {
        assertThrows(NotFoundException.class, () -> borrowingFacade.deleteBorrowing("non-existing"));
    }

    Borrowing createBorrowing() {
        User fakeUser = fakeUser(faker, UserType.CLIENT);
        BookInstance fakeBookInsance = fakeBookInstance(faker);
        Borrowing fakeBorrowing = fakeBorrowing(faker);
        fakeBorrowing.setBookInstance(fakeBookInsance);
        fakeBorrowing.setUser(fakeUser);

        BorrowingCreateDto borrowingCreateDto = BorrowingCreateDto.builder()
                .to(fakeBorrowing.getTo())
                .from(fakeBorrowing.getFrom())
                .returned(fakeBorrowing.getReturned())
                .userId(fakeBorrowing.getUser().getId())
                .bookInstanceId(fakeBorrowing.getBookInstance().getId())
                .build();

        given(userService.find(any(String.class))).willReturn(fakeUser);
        given(bookService.getInstance(any(String.class))).willReturn(fakeBookInsance);

        Borrowing borrowingResult = domainMapper.fromDto(borrowingFacade.create(borrowingCreateDto));
        assertThat(domainRepository.findById(borrowingResult.getId())).isPresent();
        assertThat(borrowingResult).isEqualTo(fakeBorrowing);

        return borrowingResult;
    }
}
