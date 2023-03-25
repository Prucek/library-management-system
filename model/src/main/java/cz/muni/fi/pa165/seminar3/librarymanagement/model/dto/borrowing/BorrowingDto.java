package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Represents borrowing DTO
 *
 * @author Marek Miček
 */
@Getter
@Setter
@SuperBuilder
public class BorrowingDto extends DomainObjectDto {

    private LocalDateTime from;

    private LocalDateTime to;

//      private BookInstanceDto bookInstance;
//
//      private UserDto user;
//
//      private FineDto fine
}
