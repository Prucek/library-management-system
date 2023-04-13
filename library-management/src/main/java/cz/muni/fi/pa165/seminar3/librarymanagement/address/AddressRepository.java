package cz.muni.fi.pa165.seminar3.librarymanagement.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface representing address repository.
 *
 * @author Peter Rúček
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
}
