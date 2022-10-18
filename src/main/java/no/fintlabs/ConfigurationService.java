package no.fintlabs;

import no.fintlabs.model.configuration.dtos.ConfigurationDto;
import no.fintlabs.model.configuration.dtos.ConfigurationPatchDto;
import no.fintlabs.model.configuration.entities.Configuration;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMappingService configurationMappingService;

    public ConfigurationService(
            ConfigurationRepository configurationRepository,
            ConfigurationMappingService configurationMappingService
    ) {
        this.configurationRepository = configurationRepository;
        this.configurationMappingService = configurationMappingService;
    }

    public boolean existsById(Long configurationId) {
        return configurationRepository.existsById(configurationId);
    }

    public Optional<ConfigurationDto> findById(Long configurationId, boolean excludeElements) {
        return configurationRepository
                .findById(configurationId)
                .map(configuration -> configurationMappingService.toConfigurationDto(configuration, excludeElements));
    }

    public Collection<ConfigurationDto> findAll(boolean excludeElements) {
        return configurationRepository
                .findAll()
                .stream()
                .map(configuration -> configurationMappingService.toConfigurationDto(configuration, excludeElements))
                .toList();
    }

    public Collection<ConfigurationDto> findByIntegrationId(Long integrationId, boolean excludeElements) {
        return configurationRepository
                .findConfigurationsByIntegrationId(integrationId)
                .stream()
                .map(configuration -> configurationMappingService.toConfigurationDto(configuration, excludeElements))
                .toList();
    }

    public ConfigurationDto save(ConfigurationDto configurationDto) {
        return configurationMappingService.toConfigurationDto(
                configurationRepository.saveWithVersion(
                        configurationMappingService.toConfiguration(configurationDto)
                ),
                false
        );
    }

    public ConfigurationDto updateById(Long configurationId, ConfigurationPatchDto configurationPatchDto) {
        Configuration configuration = configurationRepository.findById(configurationId)
                .orElseThrow(IllegalArgumentException::new);

        configurationPatchDto.getIntegrationMetadataId().ifPresent(configuration::setIntegrationMetadataId);
        configurationPatchDto.isCompleted().filter(Boolean::booleanValue).ifPresent(configuration::setCompleted);
        configurationPatchDto.getComment().ifPresent(configuration::setComment);
        configurationPatchDto.getElements().ifPresent(elementDtos -> {
            configuration.getElements().clear();
            configuration.getElements().addAll(
                    configurationMappingService.toElements(elementDtos)
            );
        });

        return configurationMappingService.toConfigurationDto(
                configurationRepository.save(configuration),
                false
        );
    }

    public void deleteById(Long configurationId) {
        configurationRepository.deleteById(configurationId);
    }

}
