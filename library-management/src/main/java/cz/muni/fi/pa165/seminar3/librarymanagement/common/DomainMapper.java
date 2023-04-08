package cz.muni.fi.pa165.seminar3.librarymanagement.common;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import java.util.List;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

/**
 * Interface for mapping generic entity to generic dto and vice-versa.
 *
 * @param <T>    entity
 * @param <DtoT> dto
 * @author Peter Rúček
 */
public interface DomainMapper<T extends DomainObject, DtoT extends DomainObjectDto> {

    /**
     * Creates a new entity from a dto.
     *
     * @param dto dto to copy values from
     * @return new entity
     */
    T fromDto(DtoT dto);

    /**
     * Creates a new dto from an entity.
     *
     * @param entity entity to copy values from
     * @return new dto
     */
    DtoT toDto(T entity);

    /**
     * Creates a list of dtos from a list of entities.
     *
     * @param entities entities to copy values from
     * @return new dto list
     */
    List<DtoT> toDtoList(List<T> entities);

    /**
     * Creates a paged result from page.
     *
     * @param source page to copy values from
     * @return new paged result
     */
    @Mappings({
            @Mapping(target = "total", expression = "java(source.getTotalElements())"),
            @Mapping(target = "page", expression = "java(source.getNumber())"),
            @Mapping(target = "pageSize", expression = "java(source.getSize())"),
            @Mapping(target = "items", expression = "java(toDtoList(source.getContent()))")
    })
    Result<DtoT> toResult(Page<T> source);
}
