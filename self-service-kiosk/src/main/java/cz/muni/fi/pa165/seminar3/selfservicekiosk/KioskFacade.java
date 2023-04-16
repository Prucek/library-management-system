package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;

/**
 * Interface representing kisok facade.
 *
 * @author Marek Fiala
 */
public interface KioskFacade {
    /**
     * Creates a borrowing.
     *
     * @param kioskBorrowDto borrowing data
     * @return created borrowing
     */
    BorrowingDto borrowBook(KioskBorrowDto kioskBorrowDto);

    /**
     * Returns the borrowing of a book instance.
     *
     * @param bookInstanceId id of the book instance
     */
    void returnBook(String bookInstanceId);
}
