package cz.muni.fi.pa165.seminar3.librarymanagement.fine;

import cz.muni.fi.pa165.seminar3.librarymanagement.borrowing.Borrowing;
import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import cz.muni.fi.pa165.seminar3.librarymanagement.payment.Payment;
import cz.muni.fi.pa165.seminar3.librarymanagement.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Objects;
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

    @NotNull
    @Positive
    private Double amount;

    @ManyToOne
    @NotNull
    private User issuer;

    @ManyToOne
    @NotNull
    private Borrowing outstandingBorrowing;

    @ManyToOne
    private Payment resolvingPayment;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fine fine)) {
            return false;
        }
        return Objects.equals(amount, fine.amount) && Objects.equals(issuer, fine.issuer) && Objects.equals(
                outstandingBorrowing, fine.outstandingBorrowing) && Objects.equals(resolvingPayment,
                fine.resolvingPayment);
    }
}
