package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.USER_SCOPE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings.SettingsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


/**
 * Tests for the Settings Controller.
 */
@WebMvcTest(controllers = {SettingsController.class, SettingsMapper.class})
public class SettingsControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SettingsFacade settingsFacade;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "SCOPE_" + USER_SCOPE)
    public void getCurrent() throws Exception {
        SettingsDto settings = SettingsDto.builder().borrowingPrice(42.0).borrowingLimit(30).finePerDay(69.0).build();
        // mock facade
        given(settingsFacade.getCurrent()).willReturn(settings);

        // perform test
        mockMvc.perform(get("/settings").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.borrowingPrice").value(settings.getBorrowingPrice()))
                .andExpect(jsonPath("$.borrowingLimit").value(settings.getBorrowingLimit()))
                .andExpect(jsonPath("$.finePerDay").value(settings.getFinePerDay()));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_" + LIBRARIAN_SCOPE)
    public void update() throws Exception {
        SettingsDto settings = SettingsDto.builder().borrowingPrice(42.0).borrowingLimit(30).finePerDay(69.0).build();
        // mock facade
        given(settingsFacade.update(any(SettingsDto.class))).willReturn(settings);

        // perform test
        mockMvc.perform(post("/settings").contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(settings)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.borrowingPrice").value(settings.getBorrowingPrice()))
                .andExpect(jsonPath("$.borrowingLimit").value(settings.getBorrowingLimit()))
                .andExpect(jsonPath("$.finePerDay").value(settings.getFinePerDay()));
    }
}
