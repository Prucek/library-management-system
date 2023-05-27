package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.AuthorUtils.fakeAuthorDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


/**
 * Tests for AuthorController.
 *
 * @author Marek Fiala
 */
@WebMvcTest(controllers = {AuthorController.class})
public class AuthorControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorFacade authorFacade;

    private final Faker faker = new Faker();

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    void createAuthorSuccessful() throws Exception {
        AuthorDto authorDto = fakeAuthorDto(faker);

        // mock facade
        given(authorFacade.create(any(AuthorCreateDto.class))).willReturn(authorDto);

        // perform test
        mockMvc.perform(post("/authors").accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));

    }

    @Test
    void findAuthorSuccessful() throws Exception {
        AuthorDto authorDto = fakeAuthorDto(faker);

        given(authorFacade.find(authorDto.getId())).willReturn(authorDto);

        mockMvc.perform(get("/authors/" + authorDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(authorDto.getName()))
                .andExpect(jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    void findAuthorNotFound() throws Exception {
        String authorId = UUID.randomUUID().toString();

        given(authorFacade.find(authorId)).willThrow(NotFoundException.class);

        mockMvc.perform(get("/authors/" + authorId)).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    void deleteAuthor() throws Exception {
        String authorId = UUID.randomUUID().toString();

        doNothing().when(authorFacade).delete(authorId);

        mockMvc.perform(delete("/authors/" + authorId).with(csrf())).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    void deleteAuthorNotFound() throws Exception {
        String authorId = UUID.randomUUID().toString();

        doThrow(NotFoundException.class).when(authorFacade).delete(any());

        mockMvc.perform(delete("/authors/" + authorId).with(csrf())).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    void updateAuthor() throws Exception {
        AuthorDto authorDto = fakeAuthorDto(faker);
        AuthorCreateDto createDto =
                AuthorCreateDto.builder().name(authorDto.getName()).surname(authorDto.getSurname()).build();

        given(authorFacade.update(eq(authorDto.getId()), any())).willReturn(authorDto);

        mockMvc.perform(put("/authors/" + authorDto.getId()).contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorDto.getId()))
                .andExpect(jsonPath("$.name").value(authorDto.getName()))
                .andExpect(jsonPath("$.surname").value(authorDto.getSurname()));

    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    void updateUserNotFound() throws Exception {
        AuthorDto authorDto = fakeAuthorDto(faker);
        AuthorCreateDto createDto =
                AuthorCreateDto.builder().name(authorDto.getName()).surname(authorDto.getSurname()).build();

        given(authorFacade.update(eq(authorDto.getId()), any())).willThrow(NotFoundException.class);

        mockMvc.perform(put("/authors/" + authorDto.getId()).contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(createDto))).andExpect(status().isNotFound());
    }
}
