package cz.muni.fi.pa165.seminar3.librarymanagement.book;

import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Book instance class.
 *
 * @author MarekFiala
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BookInstance extends DomainObject {

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bookInstance")
    private List<Borrowing> borrowings;

    @PreRemove
    private void preRemove() {
        borrowings.forEach(borrowing -> borrowing.setBookInstance(null));
    }
}
