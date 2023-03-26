package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Book entity class storing attributes for every book
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
    @OneToMany(cascade = CascadeType.ALL)
    private List<BookInstance> instances;
}
