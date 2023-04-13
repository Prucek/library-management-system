package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface representing user repository.
 *
 * @author Peter Rúček
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
