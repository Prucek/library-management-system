package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents Fine Service
 *
 * @author Juraj Marcin
 */
@Service
public class FineService extends DomainService<Fine> {

    @Getter
    private final FineRepository repository;

    @Autowired
    public FineService(FineRepository repository) {
        this.repository = repository;
    }

}
