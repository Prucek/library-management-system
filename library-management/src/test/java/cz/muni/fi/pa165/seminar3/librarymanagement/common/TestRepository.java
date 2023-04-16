package cz.muni.fi.pa165.seminar3.librarymanagement.common;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface representing test repository.
 *
 * @author Juraj Marcin
 */
public interface TestRepository extends JpaRepository<TestEntity, String> {
}
