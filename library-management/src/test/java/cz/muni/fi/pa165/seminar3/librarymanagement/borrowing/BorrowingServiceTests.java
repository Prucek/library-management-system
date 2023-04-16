package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBookInstance;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BorrowingUtils.fakeBorrowing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookInstance;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Tests for borrowing service.
 */
@WebMvcTest(controllers = {BorrowingService.class})
public class BorrowingServiceTests {

    @MockBean
    private BorrowingRepository borrowingRepository;

    @Autowired
    private BorrowingService borrowingService;

    private final Faker faker = new Faker();

    @Test
    public void findPending() {
        BookInstance bookInstance = fakeBookInstance(faker);
        Borrowing borrowing = fakeBorrowing(faker);

        // mock repository
        given(borrowingRepository.findPendingOfBookInstance(eq(bookInstance.getId()))).willReturn(List.of(borrowing));

        // perform test
        List<Borrowing> result = borrowingService.findPending(bookInstance.getId());
        assertThat(result.get(0).getId()).isEqualTo(borrowing.getId());
    }
}
