package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @DecimalMin("0.0")
    private Double borrowingPrice;

    @NotNull
    @Min(0)
    private Integer borrowingLimit;

    @NotNull
    @DecimalMin("0.0")
    private Double finePerDay;
}
