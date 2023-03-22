package cz.muni.fi.pa165.seminar3.librarymanagement.borrowing;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents borrowing entity
 * @author Marek Miček
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "domain_borrowing")
public class Borrowing extends DomainObject implements Serializable {

    @Column(name = "borrowing_from")
    private LocalDateTime from;

    @Column(name = "borrowing_to")
    private LocalDateTime to;

//    @ManyToOne
//    private User user;

//    @ManyToOne
//    private BookInstance bookInstance;
}
