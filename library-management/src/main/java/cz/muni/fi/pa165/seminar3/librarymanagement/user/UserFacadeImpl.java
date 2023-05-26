package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class representing User facade.
 *
 * @author Peter Rúček
 */
@Service
public class UserFacadeImpl extends DomainFacadeImpl<User, UserDto, UserCreateDto> implements UserFacade {

    @Getter
    private final UserService domainService;
    @Getter
    private final UserMapper domainMapper;

    /**
     * Creates a new user facade instance.
     *
     * @param domainService user service instance
     * @param domainMapper  user mapper instance
     */
    @Autowired
    public UserFacadeImpl(UserService domainService, UserMapper domainMapper) {

        this.domainService = domainService;
        this.domainMapper = domainMapper;
    }

    @Override
    public UserDto create(UserCreateDto userCreateDto) {
        return domainMapper.toDto(domainService.create(domainMapper.fromCreateDto(userCreateDto)));
    }

    @Override
    public UserDto update(String id, UserCreateDto userCreateDto) {
        User user = domainService.find(id);
        user.setUserType(userCreateDto.getUserType());
        user.setUsername(userCreateDto.getUsername());
        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setCountry(userCreateDto.getCountry());
        user.setCity(userCreateDto.getCity());
        user.setStreet(userCreateDto.getStreet());
        user.setHouseNumber(userCreateDto.getHouseNumber());
        user.setZip(userCreateDto.getZip());
        return domainMapper.toDto(domainService.update(user));
    }

    @Override
    public void delete(String userId) {
        User user = domainService.find(userId);
        domainService.delete(user);
    }
}
