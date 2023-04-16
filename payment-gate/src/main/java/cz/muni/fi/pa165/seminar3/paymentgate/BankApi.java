package cz.muni.fi.pa165.seminar3.paymentgate;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.paymentgate.CardDto;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import org.springframework.stereotype.Service;

/**
 * Class representing fake bank API.
 *
 * @author Peter Rúček
 */
@Service
public class BankApi {

    /**
     * Transfers funds from a card.
     *
     * @param cardDto card to transfer from
     * @return true if transfer was successful
     */
    public boolean transferFrom(CardDto cardDto) {
        return new Random(
                LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + cardDto.getCardNumber().hashCode()).nextDouble()
                < 0.95;
    }
}
