package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import static org.assertj.core.api.Assertions.assertThat;

import cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests for the settings repository.
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = LibraryManagementApplication.class)
@Transactional
public class SettingsRepositoryTests {
    @Autowired
    private EntityManager em;

    @Autowired
    private SettingsRepository settingsRepository;

    private Settings settings;

    /**
     * Setups entities for tests.
     */
    @BeforeEach
    public void beforeEach() {
        settings = em.merge(Settings.builder().borrowingPrice(10.).borrowingLimit(11).finePerDay(12.).build());
    }

    @Test
    public void getCurrentSettings() {
        Settings result = settingsRepository.getCurrentSettings();

        assertThat(result.getBorrowingLimit()).isEqualTo(settings.getBorrowingLimit());
        assertThat(result.getBorrowingPrice()).isEqualTo(settings.getBorrowingPrice());
        assertThat(result.getFinePerDay()).isEqualTo(settings.getFinePerDay());
    }

    /**
     * Clears the database.
     */
    @AfterEach
    public void afterEach() {
        em.remove(settings);
    }
}
