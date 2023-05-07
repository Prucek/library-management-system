package cz.muni.fi.pa165.seminar3.librarymanagement.common;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Class representing generic domain service.
 *
 * @param <T> entity to manage with the service
 * @author Peter Rúček
 */
public abstract class DomainService<T extends DomainObject> {

    /**
     * Returns the repository managing the entity.
     *
     * @return repository instance
     */
    protected abstract JpaRepository<T, String> getRepository();

    /**
     * Creates a new entity.
     *
     * @param entity entity to create
     * @return new entity
     */
    public T create(T entity) {
        return getRepository().save(entity);
    }

    /**
     * Updates an entity.
     *
     * @param entity entity to update
     * @return updated entity
     */
    public T update(T entity) {
        return getRepository().save(entity);
    }

    /**
     * Deletes an entity.
     *
     * @param entity entity to delete
     */
    public void delete(T entity) {
        getRepository().deleteById(entity.getId());
    }

    /**
     * Finds an entity with id.
     *
     * @param id id to search for
     * @return found entity
     * @throws NotFoundException if the id was not found
     */
    public T find(String id) throws NotFoundException {
        return getRepository().findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("ID %s not found", id)));
    }

    public Page<T> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }
}
