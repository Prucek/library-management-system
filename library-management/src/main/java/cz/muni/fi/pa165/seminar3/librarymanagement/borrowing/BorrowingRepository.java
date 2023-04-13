package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents borrowing JPA repository.
 *
 * @author Marek Miček
 */
@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, String> {
}
