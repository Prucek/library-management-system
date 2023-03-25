package cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.common;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Common DTO fields
 */
@Getter
@Setter
@SuperBuilder
public abstract class DomainObjectDto {

    private String id;
}
