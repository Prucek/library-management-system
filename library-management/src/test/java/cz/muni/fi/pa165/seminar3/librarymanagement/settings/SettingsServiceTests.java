package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Tests for the Settings facade implementation.
 */
@WebMvcTest(controllers = {SettingsService.class})
public class SettingsServiceTests {
    @MockBean
    private SettingsRepository settingsRepository;

    @Autowired
    private SettingsService settingsService;

    @Test
    public void getCurrentNonDefault() {
        Settings settings = Settings.builder().borrowingPrice(42.0).borrowingLimit(30).finePerDay(69.0).build();
        // mock
        given(settingsRepository.getCurrentSettings()).willReturn(settings);

        // perform
        Settings result = settingsService.getCurrent();

        assertThat(result.getBorrowingLimit()).isEqualTo(settings.getBorrowingLimit());
        assertThat(result.getBorrowingPrice()).isEqualTo(settings.getBorrowingPrice());
        assertThat(result.getFinePerDay()).isEqualTo(settings.getFinePerDay());
    }

    @Test
    public void getCurrentDefault() {
        Settings settings = Settings.getDefault();
        // mock
        given(settingsRepository.getCurrentSettings()).willReturn(null);

        // perform
        Settings result = settingsService.getCurrent();

        assertThat(result.getBorrowingLimit()).isEqualTo(settings.getBorrowingLimit());
        assertThat(result.getBorrowingPrice()).isEqualTo(settings.getBorrowingPrice());
        assertThat(result.getFinePerDay()).isEqualTo(settings.getFinePerDay());
    }

    @Test
    public void update() {
        Settings settings = Settings.getDefault();
        // mock
        given(settingsRepository.save(any(Settings.class))).willReturn(settings);

        // perform
        Settings result = settingsService.update(settings);

        assertThat(result.getBorrowingLimit()).isEqualTo(settings.getBorrowingLimit());
        assertThat(result.getBorrowingPrice()).isEqualTo(settings.getBorrowingPrice());
        assertThat(result.getFinePerDay()).isEqualTo(settings.getFinePerDay());
    }
}
