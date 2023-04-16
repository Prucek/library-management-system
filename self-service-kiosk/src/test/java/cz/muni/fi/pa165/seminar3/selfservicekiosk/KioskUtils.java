package cz.muni.fi.pa165.seminar3.selfservicekiosk;

import com.github.javafaker.Faker;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.kiosk.KioskBorrowDto;

/**
 * Class containing kiosk test utility methods.
 *
 * @author Marek Fiala
 */
public class KioskUtils {

    public static KioskBorrowDto fakeKioskBorrowingDto(Faker faker){
        return KioskBorrowDto.builder()
                .bookInstanceId(faker.internet().uuid())
                .userId(faker.internet().uuid())
                .build();
    }
}
