package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import cz.muni.fi.pa165.seminar3.librarymanagement.common.DomainObject;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class representing Settings entity.
 *
 * @author Juraj Marcin
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
public class Settings extends DomainObject {

    public static final Double DEFAULT_BORROWING_PRICE = 25.0;
    public static final Integer DEFAULT_BORROWING_LIMIT = 90;
    public static final Double DEFAULT_FINE_PER_DAY = 100.0;

    private Double borrowingPrice;

    private Integer borrowingLimit;

    private Double finePerDay;

    /**
     * Creates a Settings entity with default values.
     *
     * @return Settings entity
     */
    public static Settings getDefault() {
        return Settings.builder()
                .borrowingPrice(DEFAULT_BORROWING_PRICE)
                .borrowingLimit(DEFAULT_BORROWING_LIMIT)
                .finePerDay(DEFAULT_FINE_PER_DAY)
                .build();
    }
}
