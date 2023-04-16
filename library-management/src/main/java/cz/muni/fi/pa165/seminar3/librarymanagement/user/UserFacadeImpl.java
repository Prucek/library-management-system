package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.address.Address;
import cz.muni.fi.pa165.seminar3.librarymanagement.address.AddressMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacadeImpl;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.address.AddressDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.user.UserDto;
import java.util.ArrayList;
import java.util.List;
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
    private final AddressMapper addressMapper;

    /**
     * Creates a new user facade instance.
     *
     * @param domainService user service instance
     * @param domainMapper  user mapper instance
     */
    @Autowired
    public UserFacadeImpl(UserService domainService, UserMapper domainMapper, AddressMapper addressMapper) {

        this.domainService = domainService;
        this.domainMapper = domainMapper;
        this.addressMapper = addressMapper;
    }

    @Override
    public UserDto create(UserCreateDto userCreateDto) {
        return domainMapper.toDto(domainService.create(domainMapper.fromCreateDto(userCreateDto)));
    }

    @Override
    public UserDto update(String id, UserCreateDto userCreateDto) {
        User user = domainService.find(id);
        user.setUsername(userCreateDto.getUsername());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        List<Address> addresses = new ArrayList<>();
        for (AddressDto addressDto : userCreateDto.getAddresses()) {
            addresses.add(addressMapper.fromDto(addressDto));
        }
        user.setAddresses(addresses);
        user.setEmail(userCreateDto.getEmail());
        return domainMapper.toDto(domainService.update(user));
    }

    @Override
    public void delete(String userId) {
        User user = domainService.find(userId);
        domainService.delete(user);
    }
}
