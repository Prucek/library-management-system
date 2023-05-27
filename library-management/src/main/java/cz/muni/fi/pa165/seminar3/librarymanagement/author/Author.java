package cz.muni.fi.pa165.seminar3.librarymanagement.author;

import cz.muni.fi.pa165.seminar3.librarymanagement.book.Book;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Author entity class storing attributes for every author.
 */
@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Author extends DomainObject {
    private String name;
    private String surname;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.LAZY)
    private List<Book> publications;

    @PreRemove
    private void preRemove() {
        publications.forEach(book -> book.getAuthors().remove(this));
    }
}
