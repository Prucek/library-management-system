package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Represents borrowing JPA repository.
 *
 * @author Marek Miček
 */
@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, String> {

    @Query("""
            SELECT b FROM Borrowing b
            WHERE b.bookInstance.id = :bookInstanceId AND b.returned IS NULL
            ORDER BY b.createdAt
            """)
    List<Borrowing> findPendingOfBookInstance(@Param("bookInstanceId") String bookInstanceId);
}
