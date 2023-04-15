package cz.muni.fi.pa165.seminar3.librarymanagement.reservation;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacade;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationDto;

/**
 * Interface representing reservation facade.
 *
 * @author Marek Miček
 */
public interface ReservationFacade extends DomainFacade<ReservationDto, ReservationCreateDto> {

    /**
     * Updates a reservation.
     *
     * @param id            id of reservation to update
     * @param reservationCreateDto new borrowing values
     * @return updated borrowing
     */
    ReservationDto updateReservation(String id, ReservationCreateDto reservationCreateDto);

    /**
     * Deletes a reservation.
     *
     * @param reservationId id to delete
     */
    void deleteReservation(String reservationId);
}
