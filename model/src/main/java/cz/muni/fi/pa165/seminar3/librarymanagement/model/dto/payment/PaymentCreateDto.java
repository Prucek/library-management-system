package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateDto {

    @NotNull
    @Size(min = 1, message = "At least one fine must be included in payment")
    private List<String> fines;
}
