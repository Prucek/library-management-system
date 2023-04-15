package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BookUtils.fakeBookDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

@WebMvcTest(controllers = {BookController.class, BookMapper.class, BookInstanceMapper.class})
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
        // mock facade
        given(bookFacade.create(any(BookDto.class))).willReturn(bookDto);

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

        //mock test
        given(bookFacade.create(any(BookDto.class))).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findBookSuccessful() throws Exception {
        BookDto bookDto = fakeBookDto(faker);

        // mock services
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
        // mock services
        String bookId = UUID.randomUUID().toString();
        given(bookFacade.find(bookId)).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(get("/books/" + bookId)).andExpect(status().isNotFound());
    }

    @Test
    public void deleteBookSuccessful() throws Exception {
        BookDto bookDto = fakeBookDto(faker);
        // mock services
        doNothing().when(bookFacade).delete(bookDto.getId());

        // perform test
        mockMvc.perform(delete("/books/" + bookDto.getId())).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteBookNotFound() throws Exception {
        // mock services
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
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addBookInstance() throws Exception {
        ;
    }

    @Test
    public void removeBookInstance() throws Exception {
        String bookInstanceId = UUID.randomUUID().toString();
        // mock services
        doNothing().when(bookFacade).delete(bookInstanceId);

        // perform test
        mockMvc.perform(delete("/books/instances/" + bookInstanceId)).andExpect(status().is2xxSuccessful());
    }
}
