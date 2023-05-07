package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBook;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBookInstance;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.AuthorService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;


/**
 * Tests for BookFacade.
 *
 * @author Juraj Marcin
 */
@WebMvcTest(controllers = {BookFacade.class, BookMapper.class, BookInstanceMapper.class})
public class BookFacadeTests {

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private BookFacade bookFacade;

    private final Faker faker = new Faker();

    @Test
    public void create() throws Exception {
        Book book = fakeBook(faker);

        // mock services
        given(bookService.create(any(Book.class))).willReturn(book);
        given(authorService.find(eq(book.getAuthors().get(0).getId()))).willReturn(book.getAuthors().get(0));

        // perform test
        BookDto result = bookFacade.create(BookCreateDto.builder()
                .title(book.getTitle())
                .authorIds(book.getAuthors().stream().map(Author::getId).toList())
                .build());
        assertThat(result.getId()).isEqualTo(book.getId());
        assertThat(result.getTitle()).isEqualTo(book.getTitle());
        assertThat(result.getAuthors().get(0).getId()).isEqualTo(book.getAuthors().get(0).getId());
    }

    @Test
    public void deleteSuccessful() {
        Book book = fakeBook(faker);

        // mock services
        given(bookService.find(eq(book.getId()))).willReturn(book);

        // perform test
        bookFacade.delete(book.getId());
        verify(bookService, atLeastOnce()).delete(eq(book));
    }

    @Test
    public void deleteNotFound() {
        Book book = fakeBook(faker);

        // mock services
        given(bookService.find(eq(book.getId()))).willThrow(NotFoundException.class);

        // perform test
        assertThrows(NotFoundException.class, () -> bookFacade.delete(book.getId()));
    }

    @Test
    public void updateSuccessful() {
        Book book = fakeBook(faker);
        Book newBook = fakeBook(faker);
        newBook.setId(book.getId());

        // mock services
        given(bookService.find(eq(book.getId()))).willReturn(book);
        given(bookService.update(eq(book))).willReturn(newBook);
        given(authorService.find(eq(newBook.getAuthors().get(0).getId()))).willReturn(newBook.getAuthors().get(0));

        // perform test
        BookDto result = bookFacade.update(book.getId(), BookCreateDto.builder()
                .title(book.getTitle())
                .authorIds(newBook.getAuthors().stream().map(Author::getId).toList())
                .build());
        assertThat(result.getId()).isEqualTo(book.getId());
        assertThat(result.getTitle()).isEqualTo(newBook.getTitle());
        assertThat(result.getAuthors().get(0).getId()).isEqualTo(newBook.getAuthors().get(0).getId());
    }

    @Test
    public void updateNotFound() {
        Book book = fakeBook(faker);
        Book newBook = fakeBook(faker);
        newBook.setId(book.getId());

        // mock services
        given(bookService.find(eq(book.getId()))).willThrow(NotFoundException.class);

        // perform test
        assertThrows(NotFoundException.class, () -> bookFacade.delete(book.getId()));
    }

    @Test
    public void addInstance() {
        Book book = fakeBook(faker);
        BookInstance bookInstance = fakeBookInstance(faker);

        // mock services
        given(bookService.addInstance(eq(book.getId()))).willReturn(bookInstance);

        // perform test
        BookInstanceDto result = bookFacade.addInstance(book.getId());
        assertThat(result.getId()).isEqualTo(bookInstance.getId());
    }

    @Test
    public void removeInstance() {
        BookInstance bookInstance = fakeBookInstance(faker);

        // mock services
        given(bookService.getInstance(eq(bookInstance.getId()))).willReturn(bookInstance);

        // perform test
        bookFacade.removeInstance(bookInstance.getId());
        verify(bookService, atLeastOnce()).removeInstance(eq(bookInstance));
    }

    @Test
    public void getInstance() {
        BookInstance bookInstance = fakeBookInstance(faker);

        // mock services
        given(bookService.getInstance(eq(bookInstance.getId()))).willReturn(bookInstance);

        // perform test
        BookInstanceDto result = bookFacade.getInstance(bookInstance.getId());
        assertThat(result.getId()).isEqualTo(bookInstance.getId());
    }
}
