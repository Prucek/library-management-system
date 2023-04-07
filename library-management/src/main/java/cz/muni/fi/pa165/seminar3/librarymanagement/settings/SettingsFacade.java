package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings.SettingsDto;

/**
 * Interface representing settings facade.
 *
 * @author Juraj Marcin
 */
public interface SettingsFacade {

    /**
     * Returns current settings.
     *
     * @return current settings dto
     */
    SettingsDto getCurrent();

    /**
     * Updates the settings.
     *
     * @param settingsDto new settings dto
     * @return updated settings
     */
    SettingsDto update(SettingsDto settingsDto);
}
