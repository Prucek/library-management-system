package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import org.mapstruct.Mapper;

/**
 * Mapper mapping User DTO to User.
 *
 * @author Peter Rúček
 */
@Mapper
public interface UserMapper extends DomainMapper<User, UserDto> {

    User fromCreateDto(UserCreateDto dto);
}