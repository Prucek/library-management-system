package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Book;

import cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "domain_book")
public class Book extends DomainObject {

    private String title;

//    TODO: Array
//    @ElementCollection
//    @CollectionTable(name = "book_authors", joinColumns = @JoinColumn(name = "book_id"), foreignKey = @ForeignKey(name = "book_authors_book_fk"))
//    @Singular private List<Author> authors;

    private Author author;
}
