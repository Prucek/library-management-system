package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BorrowingUtils.fakeBorrowing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Tests for borrowing facade.
 *
 * @author Marek Fiala
 */
@WebMvcTest(controllers = {BorrowingFacadeImpl.class, BorrowingMapper.class})
public class BorrowingFacadeImplTest {

    @Autowired
    private BorrowingFacade borrowingFacade;

    @MockBean
    private BorrowingService borrowingService;

    @MockBean
    private UserService userService;

    @MockBean
    private BookService bookService;

    private final Faker faker = new Faker();

    @Test
    public void createBorrowingSuccessful() {
        Borrowing fakeBorrowing = fakeBorrowing(faker);
        // mock
        given(userService.find(eq(fakeBorrowing.getUser().getId()))).willReturn(fakeBorrowing.getUser());
        given(bookService.getInstance(eq(fakeBorrowing.getBookInstance().getId()))).willReturn(
                fakeBorrowing.getBookInstance());
        given(borrowingService.create(any(Borrowing.class))).willReturn(fakeBorrowing);

        BorrowingCreateDto borrowingCreateDto = BorrowingCreateDto.builder()
                .borrowedTo(fakeBorrowing.getBorrowedTo())
                .borrowedFrom(fakeBorrowing.getBorrowedFrom())
                .returned(fakeBorrowing.getReturned())
                .userId(fakeBorrowing.getUser().getId())
                .bookInstanceId(fakeBorrowing.getBookInstance().getId())
                .build();

        // perform
        BorrowingDto result = borrowingFacade.create(borrowingCreateDto);

        assertThat(result.getId()).isEqualTo(fakeBorrowing.getId());
        assertThat(result.getBorrowedFrom()).isEqualTo(fakeBorrowing.getBorrowedFrom());
        assertThat(result.getBorrowedTo()).isEqualTo(fakeBorrowing.getBorrowedTo());
        assertThat(result.getBookInstance().getId()).isEqualTo(fakeBorrowing.getBookInstance().getId());
        assertThat(result.getUser().getId()).isEqualTo(fakeBorrowing.getUser().getId());
        assertThat(result.getReturned()).isEqualTo(fakeBorrowing.getReturned());
    }

    @Test
    public void updateBorrowingSuccessful() {
        Borrowing borrowing = fakeBorrowing(faker);
        Borrowing newBorrowing = fakeBorrowing(faker);

        BorrowingCreateDto borrowingCreateDto = BorrowingCreateDto.builder()
                .borrowedTo(newBorrowing.getBorrowedTo())
                .borrowedFrom(newBorrowing.getBorrowedFrom())
                .returned(newBorrowing.getReturned())
                .userId(newBorrowing.getUser().getId())
                .bookInstanceId(newBorrowing.getBookInstance().getId())
                .build();

        // mock
        given(userService.find(eq(newBorrowing.getUser().getId()))).willReturn(newBorrowing.getUser());
        given(bookService.getInstance(eq(newBorrowing.getBookInstance().getId()))).willReturn(
                newBorrowing.getBookInstance());
        given(borrowingService.find(eq(borrowing.getId()))).willReturn(borrowing);
        given(borrowingService.update(any(Borrowing.class))).willReturn(newBorrowing);

        // perform
        BorrowingDto updatedBorrowingDto = borrowingFacade.updateBorrowing(borrowing.getId(), borrowingCreateDto);

        assertThat(updatedBorrowingDto.getId()).isEqualTo(newBorrowing.getId());
        assertThat(updatedBorrowingDto.getBorrowedFrom()).isEqualTo(newBorrowing.getBorrowedFrom());
        assertThat(updatedBorrowingDto.getBorrowedTo()).isEqualTo(newBorrowing.getBorrowedTo());
        assertThat(updatedBorrowingDto.getBookInstance().getId()).isEqualTo(newBorrowing.getBookInstance().getId());
        assertThat(updatedBorrowingDto.getUser().getId()).isEqualTo(newBorrowing.getUser().getId());
        assertThat(updatedBorrowingDto.getReturned()).isEqualTo(newBorrowing.getReturned());
    }

    @Test
    public void updateBorrowingNotFound() {
        Borrowing borrowing = fakeBorrowing(faker);
        BorrowingCreateDto borrowingCreateDto = BorrowingCreateDto.builder().build();
        // mock
        given(borrowingService.find(eq(borrowing.getId()))).willThrow(NotFoundException.class);

        // perform
        assertThrows(NotFoundException.class,
                () -> borrowingFacade.updateBorrowing(borrowing.getId(), borrowingCreateDto));
    }

    @Test
    public void deleteBorrowingSuccessful() {
        Borrowing borrowing = fakeBorrowing(faker);

        // mock
        given(borrowingService.find(eq(borrowing.getId()))).willReturn(borrowing);

        // perform

        borrowingFacade.deleteBorrowing(borrowing.getId());
        verify(borrowingService, atLeastOnce()).delete(eq(borrowing));
    }

    @Test
    public void deleteBorrowingNotFound() {
        // mock
        given(borrowingService.find(eq("non-existing"))).willThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> borrowingFacade.deleteBorrowing("non-existing"));
    }
}
