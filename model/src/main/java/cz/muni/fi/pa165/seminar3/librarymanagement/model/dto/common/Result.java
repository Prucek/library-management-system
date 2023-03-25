package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class Result<T extends DomainObjectDto> {

    private long total;
    private int page;
    private int pageSize;
    private List<T> items;
}