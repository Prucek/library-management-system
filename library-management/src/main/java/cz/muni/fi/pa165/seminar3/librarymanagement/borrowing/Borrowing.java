package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookInstance;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents borrowing entity.
 *
 * @author Marek Miček
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "domain_borrowing")
public class Borrowing extends DomainObject implements Serializable {

    @Column(name = "borrowing_from")
    private LocalDateTime from;

    @Column(name = "borrowing_to")
    private LocalDateTime to;

    private LocalDateTime returned;

    @ManyToOne
    private User user;

    @ManyToOne
    private BookInstance bookInstance;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Borrowing borrowing)) {
            return false;
        }
        return Objects.equals(from, borrowing.from) && Objects.equals(to, borrowing.to) && Objects.equals(returned,
                borrowing.returned) && Objects.equals(user, borrowing.user) && Objects.equals(bookInstance,
                borrowing.bookInstance);
    }

}
