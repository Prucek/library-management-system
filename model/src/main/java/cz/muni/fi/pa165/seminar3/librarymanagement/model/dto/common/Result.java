package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result<T extends DomainObjectDto> {

    private long total;
    private int page;
    private int pageSize;
    private List<T> items;

    @SafeVarargs
    public static <T extends DomainObjectDto> Result<T> of(T... elements) {
        Result<T> result = new Result<>();
        result.total = elements.length;
        result.page = 0;
        result.pageSize = elements.length;
        result.items = List.of(elements);
        return result;
    }
}