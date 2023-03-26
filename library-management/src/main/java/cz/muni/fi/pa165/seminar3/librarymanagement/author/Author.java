package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import cz.muni.fi.pa165.seminar3.librarymanagement.book.Book;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Author entity class storing attributes for every author
 */
@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
//@Embeddable
//@Table(name = "domain_author")
public class Author extends DomainObject {
    private String name;
    private String surname;
    //    @ManyToMany(mappedBy = "written_by")
    @ManyToMany()
    private List<Book> publications;
}
