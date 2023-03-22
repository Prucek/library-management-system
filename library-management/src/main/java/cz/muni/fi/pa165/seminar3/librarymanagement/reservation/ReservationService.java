package cz.muni.fi.pa165.seminar3.librarymanagement.reservation;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents reservation service which enables working with JPA repository
 * @author Marek Miček
 */
@Service
public class ReservationService extends DomainService<Reservation> {

    @Getter
    private final ReservationRepository repository;

    @Autowired
    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Reservation find(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation with id '" + id + "' not found."));
    }

}