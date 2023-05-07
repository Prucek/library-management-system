package cz.muni.fi.pa165.seminar3.paymentgate;


import static cz.muni.fi.pa165.seminar3.paymentgate.TransactionUtils.fakeCardDto;
import static cz.muni.fi.pa165.seminar3.paymentgate.TransactionUtils.fakeTransaction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common.Result;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.exceptions.NotFoundException;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.CardDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionStatus;
import cz.muni.fi.pa165.seminar3.paymentgate.transaction.Transaction;
import cz.muni.fi.pa165.seminar3.paymentgate.transaction.TransactionMapper;
import cz.muni.fi.pa165.seminar3.paymentgate.transaction.TransactionRepository;
import cz.muni.fi.pa165.seminar3.paymentgate.transaction.TransactionService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


/**
 * Tests for TransactionService.
 *
 * @author Juraj Marcin
 */
@WebMvcTest(controllers = {TransactionService.class, TransactionMapper.class})
public class TransactionServiceTests {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private BankApi bankApi;

    private final Faker faker = new Faker();

    @Test
    public void createSuccessful() {
        Transaction transaction = fakeTransaction(faker);
        // mock repository
        given(transactionRepository.save(any(Transaction.class))).willReturn(transaction);

        // perform test
        TransactionDto transactionDto = transactionService.create(TransactionCreateDto.builder()
                .amount(transaction.getAmount())
                .callbackUrl(transaction.getCallbackUrl())
                .build());
        assertThat(transactionDto.getId()).isEqualTo(transaction.getId());
        assertThat(transactionDto.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(transactionDto.getCallbackUrl()).isEqualTo(transaction.getCallbackUrl());
        assertThat(transactionDto.getStatus()).isEqualTo(transaction.getStatus());
    }

    @Test
    public void paySuccessful() {
        Transaction transaction = fakeTransaction(faker);
        CardDto cardDto = fakeCardDto(faker);
        // mock repository
        given(transactionRepository.findById(eq(transaction.getId()))).willReturn(Optional.of(transaction));
        given(transactionRepository.save(any(Transaction.class))).willReturn(transaction);
        given(bankApi.transferFrom(eq(cardDto))).willReturn(true);

        // perform test
        TransactionDto transactionDto = transactionService.pay(transaction.getId(), cardDto);

        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.APPROVED);

        assertThat(transactionDto.getId()).isEqualTo(transaction.getId());
        assertThat(transactionDto.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(transactionDto.getCallbackUrl()).isEqualTo(transaction.getCallbackUrl());
        assertThat(transactionDto.getStatus()).isEqualTo(TransactionStatus.APPROVED);
    }

    @Test
    public void payNotFound() {
        Transaction transaction = fakeTransaction(faker);
        CardDto cardDto = fakeCardDto(faker);
        // mock repository
        given(transactionRepository.findById(eq(transaction.getId()))).willReturn(Optional.empty());

        // perform test
        assertThrows(NotFoundException.class, () -> transactionService.pay(transaction.getId(), cardDto));
    }

    @Test
    public void payDeclined() {
        Transaction transaction = fakeTransaction(faker);
        CardDto cardDto = fakeCardDto(faker);
        // mock repository
        given(transactionRepository.findById(eq(transaction.getId()))).willReturn(Optional.of(transaction));
        given(transactionRepository.save(any(Transaction.class))).willReturn(transaction);
        given(bankApi.transferFrom(eq(cardDto))).willReturn(false);

        // perform test
        TransactionDto transactionDto = transactionService.pay(transaction.getId(), cardDto);

        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.DECLINED);

        assertThat(transactionDto.getId()).isEqualTo(transaction.getId());
        assertThat(transactionDto.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(transactionDto.getCallbackUrl()).isEqualTo(transaction.getCallbackUrl());
        assertThat(transactionDto.getStatus()).isEqualTo(TransactionStatus.DECLINED);
    }

    @Test
    public void findSuccessful() {
        Transaction transaction = fakeTransaction(faker);
        // mock repository
        given(transactionRepository.findById(eq(transaction.getId()))).willReturn(Optional.of(transaction));

        // perform test
        TransactionDto transactionDto = transactionService.find(transaction.getId());

        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.APPROVED);

        assertThat(transactionDto.getId()).isEqualTo(transaction.getId());
        assertThat(transactionDto.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(transactionDto.getCallbackUrl()).isEqualTo(transaction.getCallbackUrl());
        assertThat(transactionDto.getStatus()).isEqualTo(TransactionStatus.APPROVED);
    }

    @Test
    public void findNotFound() {
        Transaction transaction = fakeTransaction(faker);
        // mock repository
        given(transactionRepository.findById(eq(transaction.getId()))).willReturn(Optional.empty());

        // perform test
        assertThrows(NotFoundException.class, () -> transactionService.find(transaction.getId()));
    }

    @Test
    public void findAll() {
        List<Transaction> transactions =
                List.of(fakeTransaction(faker), fakeTransaction(faker), fakeTransaction(faker));
        Page<Transaction> transactionsPage = new PageImpl<>(transactions);
        // mock repository
        given(transactionRepository.findAll(any(Pageable.class))).willReturn(transactionsPage);

        // perform test
        Result<TransactionDto> transactionDtoResult = transactionService.findAll(0, 10);

        assertThat(transactionDtoResult.getTotal()).isEqualTo(3);
        assertThat(transactionDtoResult.getPage()).isEqualTo(0);
        assertThat(transactionDtoResult.getItems().get(0).getId()).isEqualTo(transactions.get(0).getId());
        assertThat(transactionDtoResult.getItems().get(1).getId()).isEqualTo(transactions.get(1).getId());
        assertThat(transactionDtoResult.getItems().get(2).getId()).isEqualTo(transactions.get(2).getId());
    }
}
