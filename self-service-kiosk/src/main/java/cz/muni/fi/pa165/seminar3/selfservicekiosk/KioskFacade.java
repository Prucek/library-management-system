package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;

/**
 * Interface representing kisok facade.
 *
 * @author Marek Fiala
 */
public interface KioskFacade {
    BorrowingDto borrowBook(KioskBorrowDto kioskBorrowDto);

    void returnBook(String bookInstanceId);
}
