package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class representing settings dto.
 *
 * @author Juraj Marcin
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDto {
    private Double borrowingPrice;

    private Integer borrowingLimit;

    private Double finePerDay;
}
