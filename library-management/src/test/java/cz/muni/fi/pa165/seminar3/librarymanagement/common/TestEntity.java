package cz.muni.fi.pa165.seminar3.librarymanagement.common;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Class representing a test entity.
 *
 * @author Juraj Marcin
 */
@SuperBuilder
@Entity
@NoArgsConstructor
public class TestEntity extends DomainObject {
}
