package cz.muni.fi.pa165.seminar3.librarymanagement.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Represents reservation JPA repository.
 *
 * @author Marek Miček
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
}
