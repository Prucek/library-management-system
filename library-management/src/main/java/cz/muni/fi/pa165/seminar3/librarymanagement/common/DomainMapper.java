package cz.muni.fi.pa165.seminar3.librarymanagement.common;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DomainMapper<T extends DomainObject, S extends DomainObjectDto> {

    T fromDto(S dto);

    S toDto(T entity);

    List<S> toDtoList(List<T> entities);

    @Mappings({
            @Mapping(target = "total", expression = "java(source.getTotalElements())"),
            @Mapping(target = "page", expression = "java(source.getNumber())"),
            @Mapping(target = "pageSize", expression = "java(source.getSize())"),
            @Mapping(target = "items", expression = "java(toDtoList(source.getContent()))")
    })
    Result<S> toResult(Page<T> source);
}
