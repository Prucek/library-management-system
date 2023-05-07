package cz.muni.fi.pa165.seminar3.librarymanagement.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

/**
 * Test for generic domain service.
 *
 * @author Juraj Marcin
 */

@WebMvcTest(controllers = {TestDomainService.class})
public class DomainServiceTests {
    @MockBean
    private TestRepository testRepository;

    @Autowired
    private TestDomainService testDomainService;

    @Test
    public void create() {
        TestEntity testEntity = TestEntity.builder().id(UUID.randomUUID().toString()).build();

        // mock repository
        given(testRepository.save(eq(testEntity))).willReturn(testEntity);

        // perform test
        TestEntity newTestEntity = testDomainService.create(testEntity);
        assertThat(newTestEntity.getId()).isEqualTo(testEntity.getId());
    }

    @Test
    public void update() {
        TestEntity testEntity = TestEntity.builder().id(UUID.randomUUID().toString()).build();

        // mock repository
        given(testRepository.save(eq(testEntity))).willReturn(testEntity);

        // perform test
        TestEntity newTestEntity = testDomainService.update(testEntity);
        assertThat(newTestEntity.getId()).isEqualTo(testEntity.getId());
    }

    @Test
    public void delete() {
        TestEntity testEntity = TestEntity.builder().id(UUID.randomUUID().toString()).build();

        // mock repository

        // perform test
        testDomainService.delete(testEntity);
        verify(testRepository, atLeastOnce()).deleteById(eq(testEntity.getId()));
    }

    @Test
    public void findSuccessful() {
        TestEntity testEntity = TestEntity.builder().id(UUID.randomUUID().toString()).build();
        // mock repository
        given(testRepository.findById(eq(testEntity.getId()))).willReturn(Optional.of(testEntity));

        // perform test
        TestEntity newTestEntity = testDomainService.find(testEntity.getId());
        assertThat(newTestEntity.getId()).isEqualTo(testEntity.getId());
    }

    @Test
    public void findNotFound() {
        // mock repository
        given(testRepository.findById(any())).willReturn(Optional.empty());

        // perform test
        assertThrows(NotFoundException.class, () -> testDomainService.find(UUID.randomUUID().toString()));
    }

    @Test
    public void findAll() {
        List<TestEntity> testEntities = List.of(TestEntity.builder().id(UUID.randomUUID().toString()).build(),
                TestEntity.builder().id(UUID.randomUUID().toString()).build(),
                TestEntity.builder().id(UUID.randomUUID().toString()).build());
        // mock repository
        given(testRepository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(testEntities));

        // perform test
        Page<TestEntity> result = testDomainService.findAll(Pageable.unpaged());
        assertThat(result.getTotalElements()).isEqualTo(testEntities.size());
        assertThat(result.getSize()).isEqualTo(testEntities.size());
    }
}
