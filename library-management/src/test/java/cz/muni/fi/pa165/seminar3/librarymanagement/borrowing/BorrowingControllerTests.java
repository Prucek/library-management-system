package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.BorrowingUtils.fakeBorrowing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BorrowingController.class, BorrowingMapper.class})
public class BorrowingControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingService borrowingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void findSuccessful() throws
            Exception {
        Borrowing borrowing = fakeBorrowing();
        // mock services
        given(borrowingService.find(borrowing.getId())).willReturn(borrowing);

        // perform test
        mockMvc.perform(get("/borrowings/" + borrowing.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(borrowing.getId()));
    }

    @Test
    public void findNotFound() throws
            Exception {
        // mock services
        given(borrowingService.find(any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(get("/borrowings/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    public void createSuccessful() throws
            Exception {
        Borrowing borrowing = fakeBorrowing();
        // mock services
        given(borrowingService.create(any())).willReturn(borrowing);

        // perform test
        mockMvc.perform(post("/borrowings").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BorrowingDto.builder()
                                .from(borrowing.getFrom())
                                .to(borrowing.getTo())
                                .user(UserDto.builder().id(borrowing.getUser().getId()).build())
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.user.id").value(borrowing.getUser().getId()));
    }

    @Test
    public void updateSuccessful() throws
            Exception {
        Borrowing borrowing = fakeBorrowing();
        // mock services
        given(borrowingService.find(borrowing.getId())).willReturn(borrowing);
        given(borrowingService.create(borrowing)).willReturn(borrowing);

        // perform test
        mockMvc.perform(put("/borrowings/" + borrowing.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BorrowingDto.builder()
                                .from(borrowing.getFrom())
                                .to(borrowing.getTo())
                                .user(UserDto.builder().id(borrowing.getUser().getId()).build())
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(borrowing.getId()))
                .andExpect(jsonPath("$.user.id").value(borrowing.getUser().getId()));
    }

    @Test
    public void deleteSuccessful() throws
            Exception {
        Borrowing borrowing = fakeBorrowing();
        // mock services
        given(borrowingService.find(borrowing.getId())).willReturn(borrowing);

        // perform test
        mockMvc.perform(delete("/borrowings/" + borrowing.getId())).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteNotFound() throws
            Exception {
        // mock services
        given(borrowingService.find(any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(delete("/borrowings/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    public void findAllSuccessful() throws
            Exception {
        List<Borrowing> borrowings = List.of(fakeBorrowing(), fakeBorrowing(), fakeBorrowing());
        // mock services
        given(borrowingService.findAll(any())).willReturn(new PageImpl<>(borrowings));

        // perform test
        mockMvc.perform(get("/borrowings"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.total").value(borrowings.size()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.items[0].id").value(borrowings.get(0).getId()))
                .andExpect(jsonPath("$.items[1].id").value(borrowings.get(1).getId()))
                .andExpect(jsonPath("$.items[2].id").value(borrowings.get(2).getId()));
    }
}
