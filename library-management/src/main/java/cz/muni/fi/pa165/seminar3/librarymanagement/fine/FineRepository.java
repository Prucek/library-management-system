package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents Fine JPA repository
 *
 * @author Juraj Marcin
 */
@Repository
public interface FineRepository extends JpaRepository<Fine, String> {
}
