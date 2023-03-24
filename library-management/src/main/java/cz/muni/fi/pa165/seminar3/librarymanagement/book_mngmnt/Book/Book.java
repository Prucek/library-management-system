package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Book;

import cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Singular;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Book entity class storing attributes for every book
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "domain_book")
public class Book extends DomainObject{

    private String title;
    @Singular
    @ManyToMany()
//    @JoinTable(
//            name = "written_by",
//            joinColumns = @JoinColumn(name = "book_id"),
//            inverseJoinColumns = @JoinColumn(name = "author_id")
//    )
    private List<Author> authors;
}
