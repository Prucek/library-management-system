package cz.muni.fi.pa165.seminar3.librarymanagement.payment;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionCreateDto;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.TransactionDto;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Class representing payment gate API client.
 *
 * @author Juraj Marcin
 */
@Service
public class PaymentGateApi {

    @Value("${payment-gate.url}")
    @NotBlank
    private String paymentGateUrl;

    private WebClient getWebClient() {
        return WebClient.create(paymentGateUrl);
    }

    /**
     * Requests a new transaction from the payment gate.
     *
     * @param amount      amount to pay
     * @param callbackUrl callback url to call when the transaction is finished
     * @return created transaction
     */
    public TransactionDto createTransaction(Double amount, String callbackUrl) {
        return getWebClient().post()
                .uri(uriBuilder -> uriBuilder.path("/transaction").build())
                .bodyValue(TransactionCreateDto.builder().amount(amount).callbackUrl(callbackUrl).build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TransactionDto.class)
                .block();
    }

    /**
     * Returns transaction with id.
     *
     * @param id id of transaction
     * @return found transaction
     */
    public TransactionDto findTransaction(String id) {
        return getWebClient().get()
                .uri(uriBuilder -> uriBuilder.path("/transaction/{id}").build(id))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TransactionDto.class)
                .block();
    }
}
