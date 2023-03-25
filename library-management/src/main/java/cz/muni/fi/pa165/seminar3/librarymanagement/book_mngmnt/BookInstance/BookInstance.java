package cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.BookInstance;

import cz.muni.fi.pa165.seminar3.librarymanagement.book_mngmnt.Book.Book;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookInstance extends DomainObject {
    private String ISBN;
//    Note: Asi nepotrebne jelikoz instance jsou navazany primo v jednotlivych knihach
//    @ManyToOne()
//    private Book bookReference;
}
