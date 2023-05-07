package cz.muni.fi.pa165.seminar3.librarymanagement.common;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import org.springframework.data.domain.Pageable;

/**
 * Class representing a generic facade.
 *
 * @param <T>          entity type
 * @param <DtoT>       dto type
 * @param <CreateDtoT> create dto type
 * @author Juraj Marcin
 */
public abstract class DomainFacadeImpl<T extends DomainObject, DtoT extends DomainObjectDto, CreateDtoT>
        implements DomainFacade<DtoT, CreateDtoT> {

    protected abstract DomainService<T> getDomainService();

    protected abstract DomainMapper<T, DtoT> getDomainMapper();

    @Override
    public Result<DtoT> findAll(int page, int pageSize) {
        return getDomainMapper().toResult(getDomainService().findAll(
                page >= 0 && pageSize > 0 ? Pageable.ofSize(pageSize).withPage(page) : Pageable.unpaged()));
    }

    @Override
    public DtoT find(String id) {
        return getDomainMapper().toDto(getDomainService().find(id));
    }
}
