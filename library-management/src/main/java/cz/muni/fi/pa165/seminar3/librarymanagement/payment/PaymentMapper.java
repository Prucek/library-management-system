package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment.PaymentDto;
import org.mapstruct.Mapper;

@Mapper
public interface PaymentMapper extends DomainMapper<Payment, PaymentDto> {
}
