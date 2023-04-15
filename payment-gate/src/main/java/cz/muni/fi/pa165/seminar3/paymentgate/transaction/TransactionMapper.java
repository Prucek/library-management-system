package cz.muni.fi.pa165.seminar3.paymentgate.transaction;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

/**
 * Represents Transaction mapper between Transaction entity and Transaction DTO.
 *
 * @author Peter Rúček
 */
@Mapper
public interface TransactionMapper {

    /**
     * Creates a new Transaction from a TransactionCreateDto.
     *
     * @param dto dto to copy values from
     * @return new Transaction
     */
    Transaction fromCreateDto(TransactionCreateDto dto);

    /**
     * Creates a new dto from Transaction.
     *
     * @param transaction entity to copy values from
     * @return new dto
     */
    TransactionDto toDto(Transaction transaction);

    /**
     * Creates a list of dtos from a list of Transaction entities.
     *
     * @param entities Transaction entities to copy values from
     * @return new TransactionDto list
     */
    List<TransactionDto> toDtoList(List<Transaction> entities);

    /**
     * Creates a paged result from page.
     *
     * @param source page to copy values from
     * @return new paged result
     */
    @Mappings({
            @Mapping(target = "total", expression = "java(source.getTotalElements())"),
            @Mapping(target = "page", expression = "java(source.getNumber())"),
            @Mapping(target = "pageSize", expression = "java(source.getSize())"),
            @Mapping(target = "items", expression = "java(toDtoList(source.getContent()))")
    })
    Result<TransactionDto> toResult(Page<Transaction> source);
}
