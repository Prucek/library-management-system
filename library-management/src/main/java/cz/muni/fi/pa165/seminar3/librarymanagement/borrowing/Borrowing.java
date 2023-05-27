package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookInstance;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.Fine;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.io.Serializable;
import java.time.LocalDateTime;
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
public class Borrowing extends DomainObject implements Serializable {

    private LocalDateTime borrowedFrom;

    private LocalDateTime borrowedTo;

    private LocalDateTime returned;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private BookInstance bookInstance;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "outstandingBorrowing", cascade = {CascadeType.REMOVE})
    private Fine fine;
}
