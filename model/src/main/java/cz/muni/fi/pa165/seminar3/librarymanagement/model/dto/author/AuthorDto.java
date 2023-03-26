package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author;


import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * DTO for an author. Data Transfer Object that is stable for API,
 * independent of internal Author class.
 */
@Getter
@Setter
public class AuthorDto extends DomainObjectDto {
    private String name;
    private String surname;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorDto authorDto)) return false;
        return Objects.equals(name, authorDto.name) && Objects.equals(surname, authorDto.surname);
    }
}
