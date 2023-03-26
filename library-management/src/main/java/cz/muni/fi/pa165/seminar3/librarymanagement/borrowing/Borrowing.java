package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.book.BookInstance;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents borrowing entity
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

    @ManyToOne
    private User user;

    @ManyToOne
    private BookInstance bookInstance;
}
