package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings.SettingsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class representing settings controller.
 *
 * @author Juraj Marcin
 */
@RestController
@RequestMapping(path = "/settings")
public class SettingsController {

    private final SettingsFacade settingsFacade;

    /**
     * Creates a new settings controller instance.
     *
     * @param settingsFacade settings facade instance
     */
    public SettingsController(SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
    }

    /**
     * Returns current settings.
     *
     * @return current settings
     */
    @Operation(summary = "Get current library settings")
    @ApiResponse(responseCode = "200", description = "Library Settings", useReturnTypeSchema = true)
    @GetMapping
    public SettingsDto getCurrent() {
        return settingsFacade.getCurrent();
    }

    /**
     * Updates the settings.
     *
     * @param settingsDto new settings
     * @return updated settings
     */
    @Operation(summary = "Update library settings")
    @ApiResponse(responseCode = "200", description = "Library settings updated",
            useReturnTypeSchema = true)
    @PostMapping
    public SettingsDto update(@RequestBody SettingsDto settingsDto) {
        return settingsFacade.update(settingsDto);
    }
}
