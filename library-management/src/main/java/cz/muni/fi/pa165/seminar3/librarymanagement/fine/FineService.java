package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class representing fine service.
 *
 * @author Juraj Marcin
 */
@Service
public class FineService extends DomainService<Fine> {

    @Getter
    private final FineRepository repository;

    /**
     * Creates a new fine service instance.
     *
     * @param repository fine repository instance
     */
    @Autowired
    public FineService(FineRepository repository) {
        this.repository = repository;
    }
}
