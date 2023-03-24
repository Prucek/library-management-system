package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Represents borrowing DTO
 * @author Marek Miček
 */
@Getter
@Setter
public class BorrowingDto extends DomainObjectDto {

    private LocalDateTime from;

    private LocalDateTime to;

//      private BookInstanceDto bookInstance;
//
      private UserDto user;
//
//      private FineDto fine
}
