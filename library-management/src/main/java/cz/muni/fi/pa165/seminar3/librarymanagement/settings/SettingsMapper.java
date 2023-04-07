package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings.SettingsDto;
import org.mapstruct.Mapper;


/**
 * Interface for mapping settings entity to settings dto and vice-versa.
 *
 * @author Juraj Marcin
 */
@Mapper
public interface SettingsMapper {

    /**
     * Creates a new settings entity from a dto.
     *
     * @param dto dto to copy values from
     * @return new settings entity
     */
    Settings fromDto(SettingsDto dto);

    /**
     * Creates a new settings dto from an entity.
     *
     * @param entity entity to copy values from
     * @return new settings dto
     */
    SettingsDto toDto(Settings entity);
}
