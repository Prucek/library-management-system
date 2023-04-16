package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface representing book repository.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
