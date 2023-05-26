package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book.BookInstanceDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents borrowing DTO.
 *
 * @author Marek Miček
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingDto extends DomainObjectDto {

    private LocalDateTime borrowedFrom;

    private LocalDateTime borrowedTo;

    private LocalDateTime returned;

    private BookInstanceDto bookInstance;

    private UserDto user;
}
