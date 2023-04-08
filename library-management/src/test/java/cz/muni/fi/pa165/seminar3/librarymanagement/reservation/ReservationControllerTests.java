package cz.muni.fi.pa165.seminar3.librarymanagement.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.Address;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ReservationController.class, ReservationMapper.class})
public class ReservationControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void CreateReservationSuccessful() throws Exception {
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .email("test@email.com")
                .firstName("John")
                .lastName("Malkowich")
                .username("johnM")
                .password("password")
                .userType(UserType.CLIENT)
                .address(Address.builder().city("Brno").country("CZ").street("Hrnčírska").houseNumber("99").build())
                .build();

        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID().toString())
                .from(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
                .to(LocalDateTime.now().plus(10, ChronoUnit.DAYS).truncatedTo(ChronoUnit.HOURS))
                .user(user)
                .build();

        given(reservationService.create(any())).willReturn(reservation);

        // perform test
        mockMvc.perform(post("/reservations").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ReservationCreateDto.builder()
                                .from(reservation.getFrom())
                                .to(reservation.getTo())
                                .issuerId(reservation.getUser().getId())
                                .build())))
                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.from").value(reservation.getFrom()))
//                .andExpect(jsonPath("$.to").value(reservation.getTo()))
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.user.id").value(reservation.getUser().getId()))
                .andExpect(jsonPath("$.user.email").value(reservation.getUser().getEmail()))
                .andExpect(jsonPath("$.user.firstName").value(reservation.getUser().getFirstName()))
                .andExpect(jsonPath("$.user.lastName").value(reservation.getUser().getLastName()))
        ;


    }

}
