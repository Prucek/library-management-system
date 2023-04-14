package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;
import org.mapstruct.Mapper;

/**
 * Represents Fine mapper between Fine entity and Fine DTO.
 *
 * @author Juraj Marcin
 */
@Mapper
public interface FineMapper extends DomainMapper<Fine, FineDto> {
}
