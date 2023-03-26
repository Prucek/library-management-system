package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * DTO for a borrowing action at kiosk.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KioskBorrowDto {
    private String userId;
    private String bookInstanceId;

    @Override
    public boolean equals(Object o) {
         if (this == o) return true;
         if (!(o instanceof KioskBorrowDto that)) return false;
         return Objects.equals(userId, that.userId) && Objects.equals(bookInstanceId, that.bookInstanceId);
    }

}