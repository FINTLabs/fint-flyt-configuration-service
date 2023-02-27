package no.fintlabs.mapping;

import no.fintlabs.mapping.object.ObjectMappingMappingService;
import no.fintlabs.model.configuration.dtos.ConfigurationDto;
import no.fintlabs.model.configuration.dtos.object.ObjectMappingDto;
import no.fintlabs.model.configuration.entities.Configuration;
import no.fintlabs.model.configuration.entities.object.ObjectMapping;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationMappingService {

    private final ObjectMappingMappingService objectMappingMappingService;

    public ConfigurationMappingService(ObjectMappingMappingService objectMappingMappingService) {
        this.objectMappingMappingService = objectMappingMappingService;
    }

    public Configuration toEntity(ConfigurationDto configurationDto) {
        return Configuration
                .builder()
                .integrationId(configurationDto.getIntegrationId())
                .integrationMetadataId(configurationDto.getIntegrationMetadataId())
                .comment(configurationDto.getComment())
                .completed(configurationDto.isCompleted())
                .mapping(toEntity(configurationDto.getMapping()))
                .build();
    }

    public ConfigurationDto toDto(Configuration configuration, boolean excludeMapping) {
        return ConfigurationDto
                .builder()
                .id(configuration.getId())
                .integrationId(configuration.getIntegrationId())
                .integrationMetadataId(configuration.getIntegrationMetadataId())
                .completed(configuration.isCompleted())
                .comment(configuration.getComment())
                .version(configuration.getVersion())
                .mapping(excludeMapping
                        ? null
                        : toDto(configuration.getMapping())
                )
                .build();
    }

    public ObjectMapping toEntity(ObjectMappingDto objectMappingDto) {
        return objectMappingMappingService.toEntity(objectMappingDto);
    }

    public ObjectMappingDto toDto(ObjectMapping objectMapping) {
        return objectMappingMappingService.toDto(objectMapping);
    }

}
