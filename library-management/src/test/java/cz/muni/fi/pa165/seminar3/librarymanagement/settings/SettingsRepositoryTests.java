package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import static org.assertj.core.api.Assertions.assertThat;

import cz.muni.fi.pa165.seminar3.librarymanagement.LibraryManagementApplication;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
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

    private List<Settings> settingsList;

    /**
     * Setups entities for tests.
     */
    @BeforeEach
    public void beforeEach() {
        settingsList = new ArrayList<>(4);
        settingsList.add(em.merge(Settings.builder().borrowingPrice(1.).borrowingLimit(2).finePerDay(3.).build()));
        settingsList.add(em.merge(Settings.builder().borrowingPrice(4.).borrowingLimit(5).finePerDay(6.).build()));
        settingsList.add(em.merge(Settings.builder().borrowingPrice(7.).borrowingLimit(8).finePerDay(9.).build()));
        settingsList.add(em.merge(Settings.builder().borrowingPrice(10.).borrowingLimit(11).finePerDay(12.).build()));
    }

    @Test
    public void getCurrentSettings() {
        Settings settings = settingsRepository.getCurrentSettings();

        assertThat(settings.getBorrowingLimit()).isEqualTo(settingsList.get(3).getBorrowingLimit());
        assertThat(settings.getBorrowingPrice()).isEqualTo(settingsList.get(3).getBorrowingPrice());
        assertThat(settings.getFinePerDay()).isEqualTo(settingsList.get(3).getFinePerDay());
    }

    /**
     * Clears the database.
     */
    @AfterEach
    public void afterEach() {
        settingsList.forEach(em::remove);
    }
}
