package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import cz.muni.fi.pa165.seminar3.librarymanagement.payment.Payment;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class representing Fine entity.
 *
 * @author Juraj Marcin
 */
@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Fine extends DomainObject {

    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private User issuer;

    @OneToOne(fetch = FetchType.LAZY)
    private Borrowing outstandingBorrowing;

    @ManyToOne(fetch = FetchType.LAZY)
    private Payment resolvingPayment;
}
