package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation;


import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents reservation DTO
 *
 * @author Marek Miček
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto extends DomainObjectDto {

    private LocalDateTime from;

    private LocalDateTime to;

//      private BookDto book;
//
      private UserDto user;

}
