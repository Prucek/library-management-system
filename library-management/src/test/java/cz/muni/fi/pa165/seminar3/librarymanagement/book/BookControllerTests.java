package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBookDto;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBookInstanceDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for BookController.
 *
 * @author Marek Fiala
 */
@WebMvcTest(controllers = {BookController.class})
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookFacade bookFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Test
    public void createBookSuccessful() throws Exception {
        BookDto bookDto = fakeBookDto(faker);
        // mock facades
        given(bookFacade.create(any(BookCreateDto.class))).willReturn(bookDto);

        // perform test
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(bookDto.getId()))
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.authors[0].id").value(bookDto.getAuthors().get(0).getId()));
    }

    @Test
    public void createBookNotFound() throws Exception {
        BookDto bookDto = fakeBookDto(faker);

        //mock facades
        given(bookFacade.create(any(BookCreateDto.class))).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto))).andExpect(status().isNotFound());
    }

    @Test
    public void findBookSuccessful() throws Exception {
        BookDto bookDto = fakeBookDto(faker);

        // mock facades
        given(bookFacade.find(bookDto.getId())).willReturn(bookDto);

        // perform test
        mockMvc.perform(get("/books/" + bookDto.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(bookDto.getId()))
                .andExpect(jsonPath("$.title").value(bookDto.getTitle()))
                .andExpect(jsonPath("$.authors[0].id").value(bookDto.getAuthors().get(0).getId()));
    }

    @Test
    public void findBookNotFound() throws Exception {
        // mock facades
        String bookId = UUID.randomUUID().toString();
        given(bookFacade.find(bookId)).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(get("/books/" + bookId)).andExpect(status().isNotFound());
    }

    @Test
    public void deleteBookSuccessful() throws Exception {
        BookDto bookDto = fakeBookDto(faker);
        // mock facades
        doNothing().when(bookFacade).delete(bookDto.getId());

        // perform test
        mockMvc.perform(delete("/books/" + bookDto.getId())).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteBookNotFound() throws Exception {
        // mock facades
        doThrow(EntityNotFoundException.class).when(bookFacade).delete(any());

        // perform test
        mockMvc.perform(delete("/books/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    public void updateBookSuccessful() throws Exception {
        BookDto bookDto = fakeBookDto(faker);
        BookDto bookDtoNew = fakeBookDto(faker);

        bookDtoNew.setId(bookDto.getId());

        given(bookFacade.update(eq(bookDto.getId()), any())).willReturn(bookDtoNew);

        mockMvc.perform(put("/books/" + bookDto.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDtoNew)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(bookDto.getId()))
                .andExpect(jsonPath("$.title").value(bookDtoNew.getTitle()))
                .andExpect(jsonPath("$.authors[0].id").value(bookDtoNew.getAuthors().get(0).getId()));
    }

    @Test
    public void updateBookNotFound() throws Exception {
        BookDto bookDto = fakeBookDto(faker);

        given(bookFacade.update(eq(bookDto.getId()), any())).willThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/books/" + bookDto.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookDto))).andExpect(status().isNotFound());
    }

    @Test
    public void addBookInstance() throws Exception {
        BookDto bookDto = fakeBookDto(faker);
        BookInstanceDto bookInstanceDto = fakeBookInstanceDto(faker);

        given(bookFacade.addInstance(bookDto.getId())).willReturn(bookInstanceDto);

        mockMvc.perform(post("/books/" + bookDto.getId() + "/instances").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(bookInstanceDto.getId()));
    }

    @Test
    public void removeBookInstance() throws Exception {
        String bookInstanceId = UUID.randomUUID().toString();
        // mock facades
        doNothing().when(bookFacade).delete(bookInstanceId);

        // perform test
        mockMvc.perform(delete("/books/instances/" + bookInstanceId)).andExpect(status().is2xxSuccessful());
    }
}
