package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BorrowingUtils.fakeBorrowingDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for BorrowingsController.
 *
 * @author Juraj Marcin
 */
@WebMvcTest(controllers = {BorrowingController.class, BorrowingMapper.class})
public class BorrowingControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingFacade borrowingFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Test
    public void findSuccessful() throws Exception {
        BorrowingDto borrowingDto = fakeBorrowingDto(faker);
        // mock facade
        given(borrowingFacade.find(borrowingDto.getId())).willReturn(borrowingDto);

        // perform test
        mockMvc.perform(get("/borrowings/" + borrowingDto.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(borrowingDto.getId()));
    }

    @Test
    public void findNotFound() throws Exception {
        // mock facade
        given(borrowingFacade.find(any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(get("/borrowings/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    public void createSuccessful() throws Exception {
        BorrowingDto borrowingDto = fakeBorrowingDto(faker);
        // mock facade
        given(borrowingFacade.create(any())).willReturn(borrowingDto);

        // perform test
        mockMvc.perform(post("/borrowings").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BorrowingDto.builder()
                                .from(borrowingDto.getFrom())
                                .to(borrowingDto.getTo())
                                .user(UserDto.builder().id(borrowingDto.getUser().getId()).build())
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.user.id").value(borrowingDto.getUser().getId()));
    }

    @Test
    public void updateSuccessful() throws Exception {
        BorrowingDto borrowingDto = fakeBorrowingDto(faker);
        BorrowingDto newBorrowingDto = fakeBorrowingDto(faker);
        newBorrowingDto.setId(borrowingDto.getId());

        // mock facade
        given(borrowingFacade.updateBorrowing(eq(borrowingDto.getId()), any())).willReturn(newBorrowingDto);

        // perform test
        mockMvc.perform(put("/borrowings/" + borrowingDto.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BorrowingCreateDto.builder()
                                .from(newBorrowingDto.getFrom())
                                .to(newBorrowingDto.getTo())
                                .userId(newBorrowingDto.getUser().getId())
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(newBorrowingDto.getId()))
                .andExpect(jsonPath("$.user.id").value(newBorrowingDto.getUser().getId()));
    }

    @Test
    public void deleteSuccessful() throws Exception {
        BorrowingDto borrowing = fakeBorrowingDto(faker);
        // mock facade
        given(borrowingFacade.find(borrowing.getId())).willReturn(borrowing);

        // perform test
        mockMvc.perform(delete("/borrowings/" + borrowing.getId())).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteNotFound() throws Exception {
        // mock facade
        doThrow(EntityNotFoundException.class).when(borrowingFacade).deleteBorrowing(any());

        // perform test
        mockMvc.perform(delete("/borrowings/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    public void findAllSuccessful() throws Exception {
        Result<BorrowingDto> borrowingDtoResult =
                Result.of(fakeBorrowingDto(faker), fakeBorrowingDto(faker), fakeBorrowingDto(faker));
        // mock facade
        given(borrowingFacade.findAll(eq(0), anyInt())).willReturn(borrowingDtoResult);

        // perform test
        mockMvc.perform(get("/borrowings"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.total").value(borrowingDtoResult.getTotal()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.items[0].id").value(borrowingDtoResult.getItems().get(0).getId()))
                .andExpect(jsonPath("$.items[1].id").value(borrowingDtoResult.getItems().get(1).getId()))
                .andExpect(jsonPath("$.items[2].id").value(borrowingDtoResult.getItems().get(2).getId()));
    }
}
