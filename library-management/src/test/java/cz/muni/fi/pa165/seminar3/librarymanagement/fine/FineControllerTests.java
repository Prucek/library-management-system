package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.FineUtils.fakeFineDto;
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
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for fine controller.
 */
@WebMvcTest(controllers = {FineController.class, FineMapper.class})
public class FineControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FineFacade fineFacade;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Test
    public void createFineSuccessful() throws Exception {
        FineDto fineDto = fakeFineDto(faker);
        // mock facade
        given(fineFacade.create(any(FineCreateDto.class))).willReturn(fineDto);

        // perform test
        mockMvc.perform(post("/fines").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(FineCreateDto.builder()
                                .amount(fineDto.getAmount())
                                .issuerId(fineDto.getIssuer().getId())
                                .outstandingBorrowingId(fineDto.getOutstandingBorrowing().getId())
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(fineDto.getId()))
                .andExpect(jsonPath("$.amount").value(fineDto.getAmount()))
                .andExpect(jsonPath("$.issuer.id").value(fineDto.getIssuer().getId()))
                .andExpect(jsonPath("$.outstandingBorrowing.id").value(fineDto.getOutstandingBorrowing().getId()));
    }

    @Test
    public void createFineEntityNotFound() throws Exception {
        FineDto fineDto = fakeFineDto(faker);
        // mock facade
        given(fineFacade.create(any(FineCreateDto.class))).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(post("/fines").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(FineCreateDto.builder()
                        .amount(fineDto.getAmount())
                        .issuerId(fineDto.getIssuer().getId())
                        .outstandingBorrowingId(fineDto.getOutstandingBorrowing().getId())
                        .build()))).andExpect(status().isNotFound());
    }

    @Test
    public void findAll() throws Exception {
        Result<FineDto> fineDtoResult = Result.of(fakeFineDto(faker), fakeFineDto(faker), fakeFineDto(faker));
        // mock facade
        given(fineFacade.findAll(any(Pageable.class))).willReturn(fineDtoResult);

        // perform test
        mockMvc.perform(get("/fines"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.total").value(fineDtoResult.getTotal()))
                .andExpect(jsonPath("$.page").value(fineDtoResult.getPage()))
                .andExpect(jsonPath("$.items[0].id").value(fineDtoResult.getItems().get(0).getId()))
                .andExpect(jsonPath("$.items[1].id").value(fineDtoResult.getItems().get(1).getId()))
                .andExpect(jsonPath("$.items[2].id").value(fineDtoResult.getItems().get(2).getId()));
    }

    @Test
    public void findSuccessful() throws Exception {
        FineDto fineDto = fakeFineDto(faker);
        // mock facade
        given(fineFacade.find(fineDto.getId())).willReturn(fineDto);

        // perform test
        mockMvc.perform(get("/fines/" + fineDto.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(fineDto.getId()))
                .andExpect(jsonPath("$.amount").value(fineDto.getAmount()))
                .andExpect(jsonPath("$.issuer.id").value(fineDto.getIssuer().getId()))
                .andExpect(jsonPath("$.outstandingBorrowing.id").value(fineDto.getOutstandingBorrowing().getId()));
    }

    @Test
    public void findNotFound() throws Exception {
        String fineId = UUID.randomUUID().toString();
        // mock facade
        given(fineFacade.find(fineId)).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(get("/fines/" + fineId)).andExpect(status().isNotFound());
    }

    @Test
    public void updateSuccessful() throws Exception {
        FineDto fineDto = fakeFineDto(faker);
        FineDto newFineDto = fakeFineDto(faker);
        newFineDto.setId(fineDto.getId());
        // mock facade
        given(fineFacade.updateFine(eq(fineDto.getId()), any())).willReturn(newFineDto);

        // perform test
        mockMvc.perform(put("/fines/" + fineDto.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(FineCreateDto.builder()
                                .amount(newFineDto.getAmount())
                                .issuerId(newFineDto.getIssuer().getId())
                                .outstandingBorrowingId(newFineDto.getOutstandingBorrowing().getId())
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(newFineDto.getId()))
                .andExpect(jsonPath("$.amount").value(newFineDto.getAmount()))
                .andExpect(jsonPath("$.issuer.id").value(newFineDto.getIssuer().getId()))
                .andExpect(jsonPath("$.outstandingBorrowing.id").value(newFineDto.getOutstandingBorrowing().getId()));
    }

    @Test
    public void updateNotFound() throws Exception {
        FineDto fineDto = fakeFineDto(faker);
        // mock facade
        given(fineFacade.updateFine(eq(fineDto.getId()), any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(put("/fines/" + fineDto.getId()).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(FineCreateDto.builder()
                        .amount(fineDto.getAmount())
                        .issuerId(fineDto.getIssuer().getId())
                        .outstandingBorrowingId(fineDto.getOutstandingBorrowing().getId())
                        .build()))).andExpect(status().isNotFound());
    }

    @Test
    public void deleteSuccessful() throws Exception {
        FineDto fineDto = fakeFineDto(faker);
        // mock facade
        doNothing().when(fineFacade).deleteFine(fineDto.getId());

        // perform test
        mockMvc.perform(delete("/fines/" + fineDto.getId())).andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteNotFound() throws Exception {
        // mock services
        doThrow(EntityNotFoundException.class).when(fineFacade).deleteFine(any());

        // perform test
        mockMvc.perform(delete("/fines/" + UUID.randomUUID())).andExpect(status().isNotFound());
    }
}
