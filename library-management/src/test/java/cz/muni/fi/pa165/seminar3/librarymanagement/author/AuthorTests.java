package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author.AuthorDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
    private AuthorService authorService;

    Author getExampleAuthor(){
        return Author.builder()
                .name("Alex")
                .surname("Murry")
                .build();
    }

    @Test
    void createAuthor() throws Exception {

        Author author = getExampleAuthor();

        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(author.getName());
        authorDto.setSurname(author.getSurname());

        given(authorService.create(any())).willReturn(author);

        mockMvc.perform(post("/authors")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(author.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(author.getSurname()));

    }

    @Test
    void find_all() throws Exception{
        Author author1 = getExampleAuthor();
        Author author2 = Author.builder()
                .name("Johny")
                .surname("Tick")
                .build();
        List<Author> authors = List.of(author1, author2);

        given(authorService.findAll(any())).willReturn(new PageImpl<>(authors));

        mockMvc.perform(get("/authors"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void find_author() throws Exception{
        Author author = getExampleAuthor();

        given(authorService.find(any())).willReturn(author);

        mockMvc.perform(get("/authors/" + author.getId()))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.name").value(author.getName()))
                .andExpect(jsonPath("$.surname").value(author.getSurname()));
    }

    @Test
    void delete_author() throws Exception{
        Author author = getExampleAuthor();

        given(authorService.find("/authors/" + author.getId())).willReturn(author);

        mockMvc.perform(delete("/authors/" + author.getId()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void update_user() throws Exception{
        Author author = getExampleAuthor();
        Author updatedAuthor = getExampleAuthor();
        updatedAuthor.setName("Ales");
        updatedAuthor.setSurname("Prochazka");

        given(authorService.find(any())).willReturn(author);
        given(authorService.create(author)).willReturn(updatedAuthor);

        mockMvc.perform(put("/authors/" + author.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAuthor)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value(updatedAuthor.getName()))
                .andExpect(jsonPath("$.surname").value(updatedAuthor.getSurname()));

    }
}
