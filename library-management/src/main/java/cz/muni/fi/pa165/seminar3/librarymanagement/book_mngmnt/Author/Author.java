package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Author;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Author{
    private String name;
    private String surname;
}
