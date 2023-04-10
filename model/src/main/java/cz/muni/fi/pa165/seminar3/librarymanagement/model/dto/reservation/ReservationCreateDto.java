package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateDto {

    private LocalDateTime from;

    private LocalDateTime to;

    @NotBlank
    private String bookID;

    @NotBlank
    private String issuerId;
}
