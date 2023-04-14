package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class representing reservation create dto.
 *
 * @author Marek Miček
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateDto {

    private LocalDateTime from;

    private LocalDateTime to;

    @NotBlank
    private String bookId;

    @NotBlank
    private String issuerId;
}
