package no.fintlabs;

import no.fintlabs.mapping.ConfigurationMappingService;
import no.fintlabs.model.configuration.dtos.ConfigurationDto;
import no.fintlabs.model.configuration.dtos.ConfigurationPatchDto;
import no.fintlabs.model.configuration.entities.Configuration;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMappingService configurationMappingService;
    private final ObjectMappingRepository objectMappingRepository;

    public ConfigurationService(
            ConfigurationRepository configurationRepository,
            ConfigurationMappingService configurationMappingService,
            ObjectMappingRepository objectMappingRepository) {
        this.configurationRepository = configurationRepository;
        this.configurationMappingService = configurationMappingService;
        this.objectMappingRepository = objectMappingRepository;
    }

    public Optional<ConfigurationDto> findById(Long configurationId, boolean excludeMapping) {
        return configurationRepository
                .findById(configurationId)
                .map(configuration -> configurationMappingService.toDto(configuration, excludeMapping));
    }

    public Page<ConfigurationDto> findAll(ConfigurationFilter filter, boolean excludeMapping, Pageable pageable) {
        Configuration configurationExample = new Configuration();
        filter.getIntegrationId().ifPresent(configurationExample::setIntegrationId);
        filter.getCompleted().ifPresent(configurationExample::setCompleted);

        return configurationRepository
                .findAll(
                        Example.of(configurationExample),
                        pageable
                )
                .map(configuration -> configurationMappingService.toDto(
                        configuration,
                        excludeMapping
                ));
    }

    public ConfigurationDto save(ConfigurationDto configurationDto) {
        return configurationMappingService.toDto(
                configurationRepository.saveWithVersion(
                        configurationMappingService.toEntity(configurationDto)
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
        configurationPatchDto.getMapping().map(configurationMappingService::toEntity)
                .map(objectMappingRepository::save)
                .ifPresent(configuration::setMapping);

        return configurationMappingService.toDto(
                configurationRepository.saveWithVersion(configuration),
                false
        );
    }

    public void deleteById(Long configurationId) {
        configurationRepository.deleteById(configurationId);
    }

}
