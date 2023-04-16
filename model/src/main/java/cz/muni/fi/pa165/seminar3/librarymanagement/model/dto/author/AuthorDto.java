package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.author;


import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

/**
 * DTO for an author. Data Transfer Object that is stable for API,
 * independent of internal Author class.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto extends DomainObjectDto {
    private String name;
    private String surname;

//    private List<BookDto> publications;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorDto authorDto)) return false;
        return Objects.equals(name, authorDto.name) && Objects.equals(surname, authorDto.surname);
    }
}
