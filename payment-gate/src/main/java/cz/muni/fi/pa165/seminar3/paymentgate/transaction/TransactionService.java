package cz.muni.fi.pa165.seminar3.paymentgate.transaction;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.CardDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionStatus;
import cz.muni.fi.pa165.seminar3.paymentgate.BankApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Class representing transaction service.
 *
 * @author Peter Rúček
 */
@Service
public class TransactionService {

    private final TransactionMapper mapper;

    private final TransactionRepository repository;

    private final BankApi bankApi;

    /**
     * Creates a new transaction service.
     *
     * @param mapper     transaction mapper instance
     * @param repository transaction repository instance
     * @param bankApi    bank api instance
     */
    @Autowired
    public TransactionService(TransactionMapper mapper, TransactionRepository repository, BankApi bankApi) {
        this.mapper = mapper;
        this.repository = repository;
        this.bankApi = bankApi;
    }

    /**
     * Creates a new Transaction entity.
     *
     * @param createDto transaction data
     * @return new Transaction
     */
    public TransactionDto create(TransactionCreateDto createDto) {
        return mapper.toDto(repository.save(Transaction.builder()
                .amount(createDto.getAmount())
                .callbackUrl(createDto.getCallbackUrl())
                .status(TransactionStatus.WAITING)
                .build()));
    }

    /**
     * Pays the transaction using card.
     *
     * @param id      transaction to pay
     * @param cardDto card to pay with
     * @return updated transaction
     */
    public TransactionDto pay(String id, CardDto cardDto) {
        Transaction transaction =
                repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("ID %s not found", id)));
        transaction.setStatus(bankApi.transferFrom(cardDto) ? TransactionStatus.APPROVED : TransactionStatus.DECLINED);
        return mapper.toDto(repository.save(transaction));
    }

    /**
     * Finds a Transaction with id.
     *
     * @param id id to search for
     * @return found Transaction entity
     * @throws NotFoundException if the id was not found
     */
    public TransactionDto find(String id) throws NotFoundException {
        return mapper.toDto(
                repository.findById(id).orElseThrow(() -> new NotFoundException(String.format("ID %s not found", id))));
    }

    /**
     * Finds all Transactions.
     *
     * @param page     page number
     * @param pageSize size of the page
     * @return page of found Transactions
     */
    public Result<TransactionDto> findAll(int page, int pageSize) {
        return mapper.toResult(repository.findAll(
                page >= 0 && pageSize > 0 ? Pageable.ofSize(pageSize).withPage(page) : Pageable.unpaged()));
    }
}
