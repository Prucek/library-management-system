package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * DTO for a returning action at kiosk.
 */
@Getter
@Setter
public class KioskReturnDto {
    private String bookInstanceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KioskReturnDto that)) return false;
        return Objects.equals(bookInstanceId, that.bookInstanceId);
    }
}
