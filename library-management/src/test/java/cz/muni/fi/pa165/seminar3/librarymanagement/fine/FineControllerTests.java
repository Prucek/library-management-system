package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.BorrowingService;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.UserService;
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

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.FineUtils.fakeFine;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FineController.class, FineMapper.class})
public class FineControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BorrowingService borrowingService;

    @MockBean
    private FineService fineService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createFineSuccessful() throws
            Exception {
        Fine fine = fakeFine();
        // mock services
        given(userService.find(fine.getIssuer().getId())).willReturn(fine.getIssuer());
        given(borrowingService.find(fine.getOutstandingBorrowing().getId())).willReturn(fine.getOutstandingBorrowing());
        given(fineService.create(any())).willReturn(fine);

        // perform test
        mockMvc.perform(post("/fines").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(FineCreateDto.builder()
                                .amount(fine.getAmount())
                                .issuerId(fine.getIssuer().getId())
                                .outstandingBorrowingId(fine.getOutstandingBorrowing().getId())
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(fine.getId()))
                .andExpect(jsonPath("$.amount").value(fine.getAmount()))
                .andExpect(jsonPath("$.issuer.id").value(fine.getIssuer().getId()))
                .andExpect(jsonPath("$.outstandingBorrowing.id").value(fine.getOutstandingBorrowing().getId()));
    }

    @Test
    public void createFineIssuerNotFound() throws
            Exception {
        Fine fine = fakeFine();
        // mock services
        given(userService.find(any())).willThrow(EntityNotFoundException.class);
        given(borrowingService.find(fine.getOutstandingBorrowing().getId())).willReturn(fine.getOutstandingBorrowing());
        given(fineService.create(any())).willReturn(fine);

        // perform test
        mockMvc.perform(post("/fines").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(FineCreateDto.builder()
                        .amount(fine.getAmount())
                        .issuerId(fine.getIssuer().getId())
                        .outstandingBorrowingId(fine.getOutstandingBorrowing().getId())
                        .build()))).andExpect(status().isNotFound());
    }

    @Test
    public void createFineBorrowingNotFound() throws
            Exception {
        Fine fine = fakeFine();
        // mock services
        given(userService.find(fine.getIssuer().getId())).willReturn(fine.getIssuer());
        given(borrowingService.find(any())).willThrow(EntityNotFoundException.class);
        given(fineService.create(any())).willReturn(fine);

        // perform test
        mockMvc.perform(post("/fines").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(FineCreateDto.builder()
                        .amount(fine.getAmount())
                        .issuerId(fine.getIssuer().getId())
                        .outstandingBorrowingId(fine.getOutstandingBorrowing().getId())
                        .build()))).andExpect(status().isNotFound());
    }

    @Test
    public void findAll() throws
            Exception {
        List<Fine> fines = List.of(fakeFine(), fakeFine(), fakeFine());
        // mock services
        given(fineService.findAll(any())).willReturn(new PageImpl<>(fines));

        // perform test
        mockMvc.perform(get("/fines"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.total").value(fines.size()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.items[0].id").value(fines.get(0).getId()))
                .andExpect(jsonPath("$.items[1].id").value(fines.get(1).getId()))
                .andExpect(jsonPath("$.items[2].id").value(fines.get(2).getId()));
    }

    @Test
    public void findSuccessful() throws
            Exception {
        Fine fine = fakeFine();
        // mock services
        given(fineService.find(fine.getId())).willReturn(fine);

        // perform test
        mockMvc.perform(get("/fines/" + fine.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(fine.getId()))
                .andExpect(jsonPath("$.amount").value(fine.getAmount()))
                .andExpect(jsonPath("$.issuer.id").value(fine.getIssuer().getId()))
                .andExpect(jsonPath("$.outstandingBorrowing.id").value(fine.getOutstandingBorrowing().getId()));
    }

    @Test
    public void findNotFound() throws
            Exception {
        // mock services
        given(fineService.find(any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(get("/fines/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }

    @Test
    public void updateSuccessful() throws
            Exception {
        Fine fine = fakeFine();
        Fine newFine = fakeFine();
        newFine.setId(fine.getId());
        // mock services
        given(userService.find(newFine.getIssuer().getId())).willReturn(newFine.getIssuer());
        given(borrowingService.find(newFine.getOutstandingBorrowing().getId())).willReturn(
                newFine.getOutstandingBorrowing());
        given(fineService.find(fine.getId())).willReturn(fine);
        given(fineService.update(fine)).willReturn(newFine);

        // perform test
        mockMvc.perform(put("/fines/" + fine.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(FineCreateDto.builder()
                                .amount(newFine.getAmount())
                                .issuerId(newFine.getIssuer().getId())
                                .outstandingBorrowingId(newFine.getOutstandingBorrowing().getId())
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(newFine.getId()))
                .andExpect(jsonPath("$.amount").value(newFine.getAmount()))
                .andExpect(jsonPath("$.issuer.id").value(newFine.getIssuer().getId()))
                .andExpect(jsonPath("$.outstandingBorrowing.id").value(newFine.getOutstandingBorrowing().getId()));
    }

    @Test
    public void updateNotFound() throws
            Exception {
        Fine fine = fakeFine();
        Fine newFine = fakeFine();
        newFine.setId(fine.getId());
        // mock services
        given(fineService.find(any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(put("/fines/" + UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(FineCreateDto.builder()
                        .amount(newFine.getAmount())
                        .issuerId(newFine.getIssuer().getId())
                        .outstandingBorrowingId(newFine.getOutstandingBorrowing().getId())
                        .build()))).andExpect(status().isNotFound());
    }

    @Test
    public void deleteSuccessful() throws
            Exception {
        Fine fine = fakeFine();
        // mock services
        given(fineService.find(fine.getId())).willReturn(fine);

        // perform test
        mockMvc.perform(delete("/fines/" + fine.getId())).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteNotFound() throws
            Exception {
        // mock services
        given(fineService.find(any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(delete("/fines/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }
}
