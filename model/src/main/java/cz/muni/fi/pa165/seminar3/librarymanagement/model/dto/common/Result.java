package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing paged result.
 *
 * @param <T> result type
 */
@Getter
@Setter
public class Result<T extends DomainObjectDto> {

    private long total;
    private int page;
    private int pageSize;
    private List<T> items;

    /**
     * Creates a paged result from elements.
     *
     * @param elements elements
     * @param <T>      result type
     * @return paged result
     */
    @SafeVarargs
    public static <T extends DomainObjectDto> Result<T> of(T... elements) {
        return of(List.of(elements));
    }

    /**
     * Creates a paged result from list.
     *
     * @param elements list of elements
     * @param <T>      result type
     * @return paged result
     */
    public static <T extends DomainObjectDto> Result<T> of(List<T> elements) {
        Result<T> result = new Result<>();
        result.total = elements.size();
        result.page = 0;
        result.pageSize = elements.size();
        result.items = elements;
        return result;
    }
}