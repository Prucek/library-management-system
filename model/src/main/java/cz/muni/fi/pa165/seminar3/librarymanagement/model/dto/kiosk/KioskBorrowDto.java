package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for a borrowing action at kiosk.
 */
@Getter
@Setter
public class KioskBorrowDto {
    private String userId;
    private String bookInstanceId;
}
