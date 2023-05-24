package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for a borrowing action at kiosk.
 *
 * @author MarekFiala
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class KioskBorrowDto {
    @NotBlank
    private String userId;
    @NotBlank
    private String bookInstanceId;
}
