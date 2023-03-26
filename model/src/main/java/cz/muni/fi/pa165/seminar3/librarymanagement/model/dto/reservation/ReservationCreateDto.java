package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.reservation;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCreateDto {

    private LocalDateTime from;

    private LocalDateTime to;

    //      private BookDto book;

    private String issuerId;
}
