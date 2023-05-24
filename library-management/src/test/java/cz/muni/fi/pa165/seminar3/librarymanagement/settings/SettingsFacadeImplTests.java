package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings.SettingsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Tests for the Settings facade implementation.
 */
@WebMvcTest(controllers = {SettingsFacadeImpl.class, SettingsMapper.class})
public class SettingsFacadeImplTests {
    @MockBean
    private SettingsService settingsService;

    @Autowired
    private SettingsFacade settingsFacade;

    @Test
    public void getCurrent() {
        Settings settings = Settings.getDefault();
        // mock
        given(settingsService.getCurrent()).willReturn(settings);

        // perform
        SettingsDto result = settingsFacade.getCurrent();

        assertThat(result.getBorrowingLimit()).isEqualTo(settings.getBorrowingLimit());
        assertThat(result.getBorrowingPrice()).isEqualTo(settings.getBorrowingPrice());
        assertThat(result.getFinePerDay()).isEqualTo(settings.getFinePerDay());
    }

    @Test
    public void update() {
        Settings settings = Settings.getDefault();
        // mock
        given(settingsService.update(any(Settings.class))).willReturn(settings);

        // perform
        SettingsDto result = settingsFacade.update(SettingsDto.builder()
                .borrowingPrice(settings.getBorrowingPrice())
                .borrowingLimit(settings.getBorrowingLimit())
                .finePerDay(settings.getFinePerDay())
                .build());

        assertThat(result.getBorrowingLimit()).isEqualTo(settings.getBorrowingLimit());
        assertThat(result.getBorrowingPrice()).isEqualTo(settings.getBorrowingPrice());
        assertThat(result.getFinePerDay()).isEqualTo(settings.getFinePerDay());
    }
}
