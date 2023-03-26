package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import cz.muni.fi.pa165.seminar3.librarymanagement.fine.Fine;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "domain_payment")
public class Payment extends DomainObject {

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToMany
    private List<Fine> paidFines;
}
