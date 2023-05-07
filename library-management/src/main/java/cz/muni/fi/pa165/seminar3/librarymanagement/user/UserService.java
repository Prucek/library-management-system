package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.address.AddressRepository;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class representing User service.
 *
 * @author Peter Rúček
 */
@Service
public class UserService extends DomainService<User> {

    @Getter
    private final UserRepository repository;

    @Getter
    private final AddressRepository addressRepository;

    @Autowired
    public UserService(UserRepository repository, AddressRepository addressRepository) {
        this.repository = repository;
        this.addressRepository = addressRepository;
    }

    @Override
    public User create(User user) {
        addressRepository.saveAll(user.getAddresses());
        return repository.save(user);
    }

    @Override
    public User update(User user) {
        addressRepository.saveAll(user.getAddresses());
        return repository.save(user);
    }

    @Override
    public void delete(User user) {
        addressRepository.deleteAll(user.getAddresses());
        repository.delete(user);
    }
}