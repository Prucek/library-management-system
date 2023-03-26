package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.AuthorService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BookController.class, BookMapper.class, BookInstanceMapper.class})
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void createBookSuccessful() throws Exception {

        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("Autorove");
        authorDto.setSurname("Meno");

        Author author = new Author();
        author.setName("Autorove");
        author.setSurname("Meno");

        List authorsDtoList = new ArrayList<AuthorDto>();
        authorsDtoList.add(authorDto);

        List authorsList = new ArrayList<Author>();
        authorsList.add(author);

        BookInstance bookInstance = new BookInstance();
        List bookInstanceList = new ArrayList<BookInstance>();
        bookInstanceList.add(bookInstance);

        BookInstanceDto bookInstanceDto = new BookInstanceDto();
        List bookInstanceDtoList = new ArrayList<BookInstanceDto>();
        bookInstanceDtoList.add(bookInstanceDto);

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Kniha");
        bookDto.setAuthors(authorsDtoList);
        bookDto.setInstances(bookInstanceDtoList);

        Book book = new Book();
        book.setTitle("Kniha");
        book.setAuthors(authorsList);
        book.setInstances(bookInstanceList);

        // mock services
        given(authorService.find(authorDto.getId())).willReturn(author);
        given(bookService.create(any())).willReturn(book);

        // perform test
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()));
                // TODO .andExpect(jsonPath("$.authors").value(objectMapper.writeValueAsString(authorsList)));
    }

    @Test
    public void findSuccessful() throws Exception {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("Autorove");
        authorDto.setSurname("Meno");

        Author author = new Author();
        author.setName("Autorove");
        author.setSurname("Meno");

        List authorsDtoList = new ArrayList<AuthorDto>();
        authorsDtoList.add(authorDto);

        List authorsList = new ArrayList<Author>();
        authorsList.add(author);

        BookInstance bookInstance = new BookInstance();
        List bookInstanceList = new ArrayList<BookInstance>();
        bookInstanceList.add(bookInstance);

        BookInstanceDto bookInstanceDto = new BookInstanceDto();
        List bookInstanceDtoList = new ArrayList<BookInstanceDto>();
        bookInstanceDtoList.add(bookInstanceDto);

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Kniha");
        bookDto.setAuthors(authorsDtoList);
        bookDto.setInstances(bookInstanceDtoList);

        Book book = new Book();
        book.setTitle("Kniha");
        book.setAuthors(authorsList);
        book.setInstances(bookInstanceList);
        // mock services
        given(bookService.find(book.getId())).willReturn(book);

        // perform test
        mockMvc.perform(get("/books/" + book.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()));
        // TODO .andExpect(jsonPath("$.authors").value(objectMapper.writeValueAsString(authorsList)));
    }

    @Test
    public void findNotFound() throws Exception {
        // mock services
        given(bookService.find(any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(get("/books/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    public void deleteSuccessful() throws Exception {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("Autorove");
        authorDto.setSurname("Meno");

        Author author = new Author();
        author.setName("Autorove");
        author.setSurname("Meno");

        List authorsDtoList = new ArrayList<AuthorDto>();
        authorsDtoList.add(authorDto);

        List authorsList = new ArrayList<Author>();
        authorsList.add(author);

        BookInstance bookInstance = new BookInstance();
        List bookInstanceList = new ArrayList<BookInstance>();
        bookInstanceList.add(bookInstance);

        BookInstanceDto bookInstanceDto = new BookInstanceDto();
        List bookInstanceDtoList = new ArrayList<BookInstanceDto>();
        bookInstanceDtoList.add(bookInstanceDto);

        BookDto bookDto = new BookDto();
        bookDto.setTitle("Kniha");
        bookDto.setAuthors(authorsDtoList);
        bookDto.setInstances(bookInstanceDtoList);

        Book book = new Book();
        book.setTitle("Kniha");
        book.setAuthors(authorsList);
        book.setInstances(bookInstanceList);
        // mock services
        given(bookService.find(book.getId())).willReturn(book);

        // perform test
        mockMvc.perform(delete("/books/" + book.getId())).andExpect(status().is2xxSuccessful());
    }

//    @Test
//    public void deleteNotFound() throws Exception {
//        // mock services
//        given(bookService.find(any())).willThrow(EntityNotFoundException.class);
//
//        // perform test
//        mockMvc.perform(delete("/books/" + UUID.randomUUID())).andExpect(status().isNotFound());
//    }
}
