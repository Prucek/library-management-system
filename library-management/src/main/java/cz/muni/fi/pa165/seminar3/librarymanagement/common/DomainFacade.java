package cz.muni.fi.pa165.seminar3.librarymanagement.common;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import org.springframework.data.domain.Pageable;

/**
 * Interface representing a generic facade.
 *
 * @param <DtoT>       dto type
 * @param <CreateDtoT> create dto type
 * @author Juraj Marcin
 */
public interface DomainFacade<DtoT extends DomainObjectDto, CreateDtoT> {

    /**
     * Creates a new object.
     *
     * @param createDto dto to create from
     * @return created object
     */
    DtoT create(CreateDtoT createDto);

    /**
     * Find all objects.
     *
     * @param pageable paging request
     * @return paged objects
     */
    Result<DtoT> findAll(Pageable pageable);


    /**
     * Finds an object with id.
     *
     * @param id id to search for
     * @return found object
     */
    DtoT find(String id);
}
