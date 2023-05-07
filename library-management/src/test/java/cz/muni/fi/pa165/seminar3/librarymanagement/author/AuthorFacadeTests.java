package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.AuthorUtils.fakeAuthor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Tests for AuthorFacade.
 *
 * @author Juraj Marcin
 */
@WebMvcTest(controllers = {AuthorFacade.class, AuthorMapper.class})
public class AuthorFacadeTests {

    @MockBean
    private AuthorService authorService;

    @Autowired
    private AuthorFacade authorFacade;

    private final Faker faker = new Faker();

    @Test
    public void create() {
        Author author = fakeAuthor(faker);

        // mock services
        given(authorService.create(any(Author.class))).willReturn(author);

        // perform test
        AuthorDto result = authorFacade.create(
                AuthorCreateDto.builder().name(author.getName()).surname(author.getSurname()).build());
        assertThat(result.getId()).isEqualTo(author.getId());
        assertThat(result.getName()).isEqualTo(author.getName());
        assertThat(result.getSurname()).isEqualTo(author.getSurname());
    }

    @Test
    public void updateSuccessful() {
        Author author = fakeAuthor(faker);
        Author newAuthor = fakeAuthor(faker);
        newAuthor.setId(author.getId());

        // mock services
        given(authorService.find(eq(author.getId()))).willReturn(author);
        given(authorService.update(any(Author.class))).willReturn(newAuthor);

        // perform test
        AuthorDto result = authorFacade.update(author.getId(),
                AuthorCreateDto.builder().name(newAuthor.getName()).surname(newAuthor.getSurname()).build());
        assertThat(result.getId()).isEqualTo(author.getId());
        assertThat(result.getName()).isEqualTo(newAuthor.getName());
        assertThat(result.getSurname()).isEqualTo(newAuthor.getSurname());
    }

    @Test
    public void updateNotFound() {
        Author author = fakeAuthor(faker);

        // mock services
        given(authorService.find(eq(author.getId()))).willThrow(NotFoundException.class);

        // perform test
        assertThrows(NotFoundException.class, () -> authorFacade.update(author.getId(),
                AuthorCreateDto.builder().name(author.getName()).surname(author.getSurname()).build()));
    }

    @Test
    public void deleteSuccessful() {
        Author author = fakeAuthor(faker);

        // mock services
        given(authorService.find(eq(author.getId()))).willReturn(author);
        doNothing().when(authorService).delete(eq(author));

        // perform test
        authorFacade.delete(author.getId());
        verify(authorService, atLeastOnce()).delete(eq(author));
    }

    @Test
    public void deleteNotFound() {
        Author author = fakeAuthor(faker);

        // mock services
        given(authorService.find(eq(author.getId()))).willThrow(NotFoundException.class);

        // perform test
        assertThrows(NotFoundException.class, () -> authorFacade.delete(author.getId()));
    }
}
