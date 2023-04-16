package cz.muni.fi.pa165.seminar3.librarymanagement.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class representing test service.
 *
 * @author Juraj Marcin
 */
@Service
public class TestDomainService extends DomainService<TestEntity> {

    @Getter
    private final TestRepository repository;

    /**
     * Creates a new test service.
     *
     * @param repository test repository instance
     */
    @Autowired
    public TestDomainService(TestRepository repository) {
        this.repository = repository;
    }
}
