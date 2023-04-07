package cz.muni.fi.pa165.seminar3.librarymanagement.settings;

import cz.muni.fi.pa165.seminar3.librarymanagement.model.dto.settings.SettingsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class representing settings facade.
 *
 * @author Juraj Marcin
 */
@Service
public class SettingsFacadeImpl implements SettingsFacade {

    private final SettingsService domainService;
    private final SettingsMapper domainMapper;

    /**
     * Creates a new settings facade instance.
     *
     * @param domainService settings service instance
     * @param domainMapper  service mapper instance
     */
    @Autowired
    public SettingsFacadeImpl(SettingsService domainService,
                              SettingsMapper domainMapper) {
        this.domainService = domainService;
        this.domainMapper = domainMapper;
    }

    @Override
    public SettingsDto getCurrent() {
        return domainMapper.toDto(domainService.getCurrent());
    }

    @Override
    public SettingsDto update(SettingsDto settingsDto) {
        return domainMapper.toDto(domainService.update(domainMapper.fromDto(settingsDto)));
    }
}
