package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for a returning action at kiosk.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KioskReturnDto {
    private String bookInstanceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KioskReturnDto that)) {
            return false;
        }
        return Objects.equals(bookInstanceId, that.bookInstanceId);
    }
}
