package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Author;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
//@Table(name = "domain_author")
public class Author extends DomainObject {
    private String name;
    private String surname;
}
