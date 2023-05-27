package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.LIBRARIAN_SCOPE;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_BEARER;
import static cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.AuthConstants.SECURITY_SCHEME_OAUTH2;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.ErrorMessage;
import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings.SettingsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
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
@ApiResponse(responseCode = "401", description = "Authentication is required")
@ApiResponse(responseCode = "403", description = "Access token does not have required scope")
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
    @SecurityRequirement(name = SECURITY_SCHEME_BEARER, scopes = {LIBRARIAN_SCOPE})
    @SecurityRequirement(name = SECURITY_SCHEME_OAUTH2, scopes = {LIBRARIAN_SCOPE})
    @ApiResponse(responseCode = "200", description = "Library settings updated", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "400", description = "Invalid payload",
            content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    @PostMapping
    public SettingsDto update(@RequestBody @Valid SettingsDto settingsDto) {
        return settingsFacade.update(settingsDto);
    }
}
