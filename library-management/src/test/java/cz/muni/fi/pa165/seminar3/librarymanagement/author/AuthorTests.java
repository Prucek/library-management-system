package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.AuthorController;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.AuthorFacade;
import cz.muni.fi.pa165.seminar3.librarymanagement.author.AuthorMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.AuthorUtils.fakeAuthorDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthorController.class, AuthorMapper.class})
public class AuthorTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorFacade authorFacade;

    private final Faker faker = new Faker();

    @Test
    void createAuthorSuccessful() throws Exception {
        AuthorDto authorDto = fakeAuthorDto(faker);

        // mock facade
        given(authorFacade.create(any(AuthorDto.class))).willReturn(authorDto);

        // perform test
        mockMvc.perform(post("/authors")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));

    }

    @Test
    void createAuthorNotFound() throws Exception {
        AuthorDto authorDto = fakeAuthorDto(faker);

        // mock facade
        given(authorFacade.create(any(AuthorDto.class))).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(post("/authors")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto))
                )
                .andExpect(status().isNotFound());

    }
    @Test
    void findAuthorSuccessful() throws Exception{
        AuthorDto authorDto = fakeAuthorDto(faker);

        given(authorFacade.find(authorDto.getId())).willReturn(authorDto);

        mockMvc.perform(get("/authors/" + authorDto.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value(authorDto.getName()))
                .andExpect(jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    void findAuthorNotFound() throws Exception{
        String authorId = UUID.randomUUID().toString();

        given(authorFacade.find(authorId)).willThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/authors/" + authorId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAuthor() throws Exception{
        String authorId = UUID.randomUUID().toString();

        doNothing().when(authorFacade).delete(authorId);

        mockMvc.perform(delete("/authors/" + authorId))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteAuthorNotFound() throws Exception{
        String authorId = UUID.randomUUID().toString();

        doThrow(EntityNotFoundException.class).when(authorFacade).delete(any());

        mockMvc.perform(delete("/authors/" + authorId))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser() throws Exception{
        AuthorDto authorDto = fakeAuthorDto(faker);
        AuthorDto authorDtoNew = fakeAuthorDto(faker);

        authorDtoNew.setId(authorDto.getId());

        given(authorFacade.update(eq(authorDto.getId()), any())).willReturn(authorDtoNew);

        mockMvc.perform(put("/authors/" + authorDto.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDtoNew)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(authorDto.getId()))
                .andExpect(jsonPath("$.name").value(authorDtoNew.getName()))
                .andExpect(jsonPath("$.surname").value(authorDtoNew.getSurname()));

    }

    @Test
    void updateUserNotFound() throws Exception{
        AuthorDto authorDto = fakeAuthorDto(faker);

        given(authorFacade.update(eq(authorDto.getId()), any())).willThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/authors/" + authorDto.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isNotFound());

    }
}
