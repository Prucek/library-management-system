package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class representing fine dto.
 *
 * @author Juraj Marcin
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FineDto extends DomainObjectDto {

    private Double amount;

    private UserDto issuer;

    private BorrowingDto outstandingBorrowing;
}

