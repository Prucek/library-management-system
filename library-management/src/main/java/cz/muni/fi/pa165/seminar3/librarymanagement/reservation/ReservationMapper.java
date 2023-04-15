package cz.muni.fi.pa165.seminar3.librarymanagement.reservation;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation.ReservationDto;
import org.mapstruct.Mapper;

/**
 * Represents reservation mapper between Reservation entity and Reservation DTO.
 *
 * @author Marek Miček
 */
@Mapper
public interface ReservationMapper extends DomainMapper<Reservation, ReservationDto> {
}
