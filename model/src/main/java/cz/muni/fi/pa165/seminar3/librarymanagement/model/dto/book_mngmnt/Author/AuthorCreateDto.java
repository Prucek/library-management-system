package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.book_mngmnt.Author;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.DomainObjectDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorCreateDto extends DomainObjectDto {
    private String name;
    private String surname;
}
