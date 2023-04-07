package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Interface representing settings repository.
 *
 * @author Juraj Marcin
 */
public interface SettingsRepository extends JpaRepository<Settings, String> {

    /**
     * Finds the most up-to-date settings.
     *
     * @return current Settings entity
     */
    @Query("SELECT s FROM Settings s ORDER BY s.createdAt DESC LIMIT 1")
    Settings getCurrentSettings();
}
