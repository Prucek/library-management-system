package cz.muni.fi.pa165.seminar3.librarymanagement.reservation;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents reservation entity
 * @author Marek Miček
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "domain_reservation")
public class Reservation extends DomainObject {

    @Column(name = "reservation_from")
    private LocalDateTime from;

    @Column(name = "reservation_to")
    private LocalDateTime to;

//    @ManyToOne
//    private User user;

//    @ManyToOne
//    private Book book;
}