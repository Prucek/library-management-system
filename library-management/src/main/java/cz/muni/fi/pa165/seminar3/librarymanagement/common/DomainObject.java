package cz.muni.fi.pa165.seminar3.librarymanagement.common;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class DomainObject {

    @Id
    private String id = UUID.randomUUID().toString();
}
