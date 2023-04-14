package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


/**
 * Class representing borrowing create dto.
 *
 * @author Marek Miček
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingCreateDto {

    private LocalDateTime from;

    private LocalDateTime to;

    private LocalDateTime returned;

    @NotBlank
    private String bookInstanceId;

    @NotBlank
    private String userId;

    @NotBlank
    private String fineId;
}
