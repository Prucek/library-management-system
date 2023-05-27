package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.author.Author;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import cz.muni.fi.pa165.seminar3.librarymanagement.reservation.Reservation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Book extends DomainObject {

    private String title;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Author> authors;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<BookInstance> instances;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    private List<Reservation> reservations;

    @PreRemove
    private void preRemove() {
        authors.forEach(author -> author.getPublications().remove(this));
        reservations.forEach(reservation -> reservation.setBook(null));
    }
}
