package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacade;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import java.util.List;

/**
 * Interface representing borrowing facade.
 *
 * @author Marek Miček
 */
public interface BorrowingFacade extends DomainFacade<BorrowingDto, BorrowingCreateDto> {

    /**
     * Updates a borrowing.
     *
     * @param id                 id of borrowing to update
     * @param borrowingCreateDto new borrowing values
     * @return updated borrowing
     */
    BorrowingDto updateBorrowing(String id, BorrowingCreateDto borrowingCreateDto);

    /**
     * Deletes a borrowing.
     *
     * @param borrowingId id to delete
     */
    void deleteBorrowing(String borrowingId);

    /**
     * Finds pending borrowings.
     *
     * @param bookInstanceId id of the book instance
     * @return pending borrowings
     */
    List<BorrowingDto> findPending(String bookInstanceId);
}
