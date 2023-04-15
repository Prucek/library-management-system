package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.borrowing.BorrowingDto;
import org.mapstruct.Mapper;

/**
 * Represents borrowing mapper between Borrowing entity and Borrowing DTO.
 *
 * @author Marek Miček
 */
@Mapper
public interface BorrowingMapper extends DomainMapper<Borrowing, BorrowingDto> {
}
