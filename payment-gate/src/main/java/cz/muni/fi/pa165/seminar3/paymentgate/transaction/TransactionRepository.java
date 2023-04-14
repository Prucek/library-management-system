package cz.muni.fi.pa165.seminar3.paymentgate.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface representing transaction repository.
 *
 * @author Peter Rúček
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
