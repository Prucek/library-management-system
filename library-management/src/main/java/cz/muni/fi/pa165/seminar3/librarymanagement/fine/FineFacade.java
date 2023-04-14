package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainFacade;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.fine.FineDto;

/**
 * Interface representing fine facade.
 *
 * @author Juraj Marcin
 */
public interface FineFacade extends DomainFacade<FineDto, FineCreateDto> {

    /**
     * Updates a fine.
     *
     * @param id            id of fine to update
     * @param fineCreateDto new fine values
     * @return updated fine
     */
    FineDto update(String id, FineCreateDto fineCreateDto);

    /**
     * Deletes a fine.
     *
     * @param fineId id to delete
     */
    void delete(String fineId);
}
