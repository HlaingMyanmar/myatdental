package org.myatdental.systemConfig;

import org.myatdental.systemConfig.model.SystemSetting;
import org.myatdental.systemConfig.repository.SystemSettingRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
public class SettingsController {

    private final SystemSettingRepository settingRepository;

    public SettingsController(SystemSettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @GetMapping("/settings")
    public Map<String, String> getPublicSettings() {
        List<SystemSetting> settings = settingRepository.findAll();
        // Return as Key:Value Map for Frontend
        return settings.stream()
                .collect(Collectors.toMap(SystemSetting::getSettingKey,
                        s -> s.getSettingValue() != null ? s.getSettingValue() : ""));
    }
}