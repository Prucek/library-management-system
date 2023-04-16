package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface representing a book instance repository.
 *
 * @author Juraj Marcin
 */
@Repository
public interface BookInstanceRepository extends JpaRepository<BookInstance, String> {
}
