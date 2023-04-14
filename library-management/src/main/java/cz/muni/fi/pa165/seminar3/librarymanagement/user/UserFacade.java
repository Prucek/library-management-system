package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacade;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;

/**
 * Interface representing user facade.
 *
 * @author Peter Rúček
 */
public interface UserFacade extends DomainFacade<UserDto, UserCreateDto> {

    /**
     * Updates a user.
     *
     * @param id            id of user to update
     * @param userCreateDto new user values
     * @return updated user
     */
    UserDto update(String id, UserCreateDto userCreateDto);

    /**
     * Deletes a user.
     *
     * @param userId id to delete
     */
    void delete(String userId);
}
