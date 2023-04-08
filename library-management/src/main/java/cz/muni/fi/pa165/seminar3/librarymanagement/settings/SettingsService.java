package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class representing settings service.
 *
 * @author Juraj Marcin
 */
@Service
public class SettingsService {

    private final SettingsRepository repository;

    /**
     * Creates a new settings service instance.
     *
     * @param repository settings repository instance
     */
    @Autowired
    public SettingsService(SettingsRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns an entity with current settings.
     *
     * @return current settings entity
     */
    public Settings getCurrent() {
        Settings settings = repository.getCurrentSettings();
        return settings != null ? settings : Settings.getDefault();
    }

    /**
     * Updates the settings.
     *
     * @param newSettings new settings entity
     * @return updated settings entity
     */
    public Settings update(Settings newSettings) {
        return repository.save(newSettings);
    }
}
