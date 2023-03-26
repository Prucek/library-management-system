package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Class representing Payment JPA repository
 *
 * @author Juraj Marcin
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
}
