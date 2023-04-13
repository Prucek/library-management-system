package cz.muni.fi.pa165.seminar3.librarymanagement.address;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.address.AddressDto;
import org.mapstruct.Mapper;

/**
 * Mapper mapping Address DTO to Address.
 *
 * @author Peter Rúček
 */
@Mapper
public interface AddressMapper extends DomainMapper<Address, AddressDto> {

}
