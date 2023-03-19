package cz.muni.fi.pa165.seminar3.librarymanagement.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public abstract class DomainService<T extends DomainObject> {

    public static final int DEFAULT_PAGE_SIZE = 10;

    public abstract JpaRepository<T, String> getRepository();

    @Transactional
    public T create(T entity) {
        return getRepository().save(entity);
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<T> findAll(int page) {
        return getRepository().findAll(PageRequest.of(page, DEFAULT_PAGE_SIZE));
    }
}
