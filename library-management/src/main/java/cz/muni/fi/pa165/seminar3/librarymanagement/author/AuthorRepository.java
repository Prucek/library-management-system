package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface representing author repository.
 *
 * @author Marek Fiala
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {
}
