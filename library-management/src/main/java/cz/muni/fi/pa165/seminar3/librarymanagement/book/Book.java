package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;


/**
 * Book entity class storing attributes for every book.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "domain_book")
public class Book extends DomainObject {

    private String title;
    @Singular
    @ManyToMany()
    //    @JoinTable(
    //            name = "written_by",
    //            joinColumns = @JoinColumn(name = "book_id"),
    //            inverseJoinColumns = @JoinColumn(name = "author_id")
    //    )
    private List<Author> authors;

    @Singular
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookInstance> instances;
}
