package cz.muni.fi.pa165.seminar3.librarymanagement.book;


import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBook;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBookInstance;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import com.github.javafaker.Faker;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Tests for BookService.
 *
 * @author Juraj Marcin
 */
@WebMvcTest(controllers = {BookService.class})
public class BookServiceTests {

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookInstanceRepository bookInstanceRepository;

    @Autowired
    private BookService bookService;

    private final Faker faker = new Faker();

    @Test
    public void addInstance() {
        Book book = fakeBook(faker);
        BookInstance bookInstance = fakeBookInstance(faker);

        // mock repositories
        given(bookRepository.findById(book.getId())).willReturn(Optional.of(book));
        given(bookInstanceRepository.save(any(BookInstance.class))).willReturn(bookInstance);

        // perform test
        BookInstance result = bookService.addInstance(book.getId());
        assertThat(result.getId()).isEqualTo(bookInstance.getId());
    }

    @Test
    public void getInstance() {
        BookInstance bookInstance = fakeBookInstance(faker);

        // mock repositories
        given(bookInstanceRepository.findById(bookInstance.getId())).willReturn(Optional.of(bookInstance));

        // perform test
        BookInstance result = bookService.getInstance(bookInstance.getId());
        assertThat(result.getId()).isEqualTo(bookInstance.getId());
    }

    @Test
    public void removeInstance() {
        BookInstance bookInstance = fakeBookInstance(faker);

        // mock repositories

        // perform test
        bookService.removeInstance(bookInstance);
        verify(bookInstanceRepository, atLeastOnce()).delete(bookInstance);
    }
}
