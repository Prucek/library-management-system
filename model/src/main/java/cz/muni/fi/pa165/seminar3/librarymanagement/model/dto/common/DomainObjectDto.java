package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class representing a generic dto.
 *
 * @author Peter Rúček
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class DomainObjectDto {

    private String id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
