package cz.muni.fi.pa165.seminar3.librarymanagement.common;


import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class DomainObject {

    @Id
    private String id = UUID.randomUUID().toString();
}
