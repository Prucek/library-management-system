package cz.muni.fi.pa165.seminar3.librarymanagement.user;

import cz.muni.fi.pa165.seminar3.librarymanagement.address.AddressRepository;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class representing User service.
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

    @Transactional(readOnly = true)
    public User find(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User '" + id + "' not found."));
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