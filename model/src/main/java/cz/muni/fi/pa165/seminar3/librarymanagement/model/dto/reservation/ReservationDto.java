package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation;


import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents reservation DTO
 * @author Marek Miček
 */
@Getter
@Setter
public class ReservationDto extends DomainObjectDto {

    private LocalDateTime from;

    private LocalDateTime to;

//      private BookDto book;
//
//      private UserDto user;

}
