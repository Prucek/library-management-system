package cz.muni.fi.pa165.seminar3.librarymanagement.reservation;

import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication.USER_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.utils.ReservationUtils.fakeReservationDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for ReservationController.
 *
 * @author Marek Miček
 */
@WebMvcTest(controllers = {ReservationController.class, ReservationMapper.class})
public class ReservationControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationFacade reservationFacade;

    private final Faker faker = new Faker();

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void findSuccessful() throws Exception {
        ReservationDto reservationDto = fakeReservationDto(faker);
        // mock facade
        given(reservationFacade.find(reservationDto.getId())).willReturn(reservationDto);

        // perform test
        mockMvc.perform(get("/reservations/" + reservationDto.getId()).with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(reservationDto.getId()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void findNotFound() throws Exception {
        // mock facade
        given(reservationFacade.find(any())).willThrow(EntityNotFoundException.class);

        // perform test
        mockMvc.perform(get("/reservations/" + UUID.randomUUID()).with(csrf())).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    public void createSuccessful() throws Exception {
        ReservationDto reservationDto = fakeReservationDto(faker);
        // mock facade
        given(reservationFacade.create(any())).willReturn(reservationDto);

        // perform test
        mockMvc.perform(post("/reservations").contentType(MediaType.APPLICATION_JSON).with(csrf())
                        .content(objectMapper.writeValueAsString(BorrowingDto.builder()
                                .from(reservationDto.getFrom())
                                .to(reservationDto.getTo())
                                .user(UserDto.builder().id(reservationDto.getUser().getId()).build())
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.user.id").value(reservationDto.getUser().getId()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    public void updateSuccessful() throws Exception {
        ReservationDto reservationDto = fakeReservationDto(faker);
        ReservationDto newReservationDto = fakeReservationDto(faker);
        newReservationDto.setId(reservationDto.getId());

        // mock facade
        given(reservationFacade.updateReservation(eq(reservationDto.getId()), any())).willReturn(newReservationDto);

        // perform test
        mockMvc.perform(put("/reservations/" + reservationDto.getId()).contentType(MediaType.APPLICATION_JSON).with(csrf())
                        .content(objectMapper.writeValueAsString(BorrowingCreateDto.builder()
                                .from(newReservationDto.getFrom())
                                .to(newReservationDto.getTo())
                                .userId(newReservationDto.getUser().getId())
                                .build())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(newReservationDto.getId()))
                .andExpect(jsonPath("$.user.id").value(newReservationDto.getUser().getId()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    public void deleteSuccessful() throws Exception {
        ReservationDto reservationDto = fakeReservationDto(faker);
        // mock facade
        given(reservationFacade.find(reservationDto.getId())).willReturn(reservationDto);

        // perform test
        mockMvc.perform(delete("/reservations/" + reservationDto.getId()).with(csrf())).andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    public void deleteNotFound() throws Exception {
        // mock facade
        doThrow(EntityNotFoundException.class).when(reservationFacade).deleteReservation(any());

        // perform test
        mockMvc.perform(delete("/reservations/" + UUID.randomUUID()).with(csrf())).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void findAllSuccessful() throws Exception {
        Result<ReservationDto> reservationDtoResult =
                Result.of(fakeReservationDto(faker), fakeReservationDto(faker), fakeReservationDto(faker));
        // mock facade
        given(reservationFacade.findAll(any(Pageable.class))).willReturn(reservationDtoResult);

        // perform test
        mockMvc.perform(get("/reservations").with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.total").value(reservationDtoResult.getTotal()))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.items[0].id").value(reservationDtoResult.getItems().get(0).getId()))
                .andExpect(jsonPath("$.items[1].id").value(reservationDtoResult.getItems().get(1).getId()))
                .andExpect(jsonPath("$.items[2].id").value(reservationDtoResult.getItems().get(2).getId()));
    }
}
