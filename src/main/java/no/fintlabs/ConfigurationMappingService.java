package no.fintlabs;

import no.fintlabs.model.configuration.dtos.CollectionFieldConfigurationDto;
import no.fintlabs.model.configuration.dtos.ConfigurationDto;
import no.fintlabs.model.configuration.dtos.ConfigurationElementDto;
import no.fintlabs.model.configuration.dtos.FieldConfigurationDto;
import no.fintlabs.model.configuration.entities.CollectionFieldConfiguration;
import no.fintlabs.model.configuration.entities.Configuration;
import no.fintlabs.model.configuration.entities.ConfigurationElement;
import no.fintlabs.model.configuration.entities.FieldConfiguration;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ConfigurationMappingService {

    public Configuration toConfiguration(ConfigurationDto configurationDto) {
        return Configuration
                .builder()
                .integrationId(configurationDto.getIntegrationId())
                .integrationMetadataId(configurationDto.getIntegrationMetadataId())
                .completed(configurationDto.isCompleted())
                .elements(toElements(configurationDto.getElements()))
                .build();
    }

    public Collection<ConfigurationElement> toElements(Collection<ConfigurationElementDto> configurationElementDtos) {
        return configurationElementDtos
                .stream()
                .map(this::toElement)
                .toList();
    }

    public ConfigurationElement toElement(ConfigurationElementDto configurationElementDto) {
        return ConfigurationElement
                .builder()
                .key(configurationElementDto.getKey())
                .elements(toElements(configurationElementDto.getElements()))
                .fieldConfigurations(toFieldConfigurations(configurationElementDto.getFieldConfigurations()))
                .collectionFieldConfigurations(toCollectionFieldConfigurations(configurationElementDto.getCollectionFieldConfigurations()))
                .build();
    }

    private Collection<FieldConfiguration> toFieldConfigurations(Collection<FieldConfigurationDto> fieldConfigurationDtos) {
        return fieldConfigurationDtos
                .stream()
                .map(this::toFieldConfiguration)
                .toList();
    }

    private FieldConfiguration toFieldConfiguration(FieldConfigurationDto fieldConfigurationDto) {
        return FieldConfiguration
                .builder()
                .key(fieldConfigurationDto.getKey())
                .type(fieldConfigurationDto.getType())
                .value(fieldConfigurationDto.getValue())
                .build();
    }

    private Collection<CollectionFieldConfiguration> toCollectionFieldConfigurations(Collection<CollectionFieldConfigurationDto> collectionFieldConfigurationDtos) {
        return collectionFieldConfigurationDtos
                .stream()
                .map(this::toCollectionFieldConfiguration)
                .toList();
    }

    private CollectionFieldConfiguration toCollectionFieldConfiguration(CollectionFieldConfigurationDto collectionFieldConfigurationDto) {
        return CollectionFieldConfiguration
                .builder()
                .key(collectionFieldConfigurationDto.getKey())
                .type(collectionFieldConfigurationDto.getType())
                .values(collectionFieldConfigurationDto.getValues())
                .build();
    }

    public ConfigurationDto toConfigurationDto(Configuration configuration, boolean excludeElements) {
        return ConfigurationDto
                .builder()
                .id(configuration.getId())
                .integrationId(configuration.getIntegrationId())
                .integrationMetadataId(configuration.getIntegrationMetadataId())
                .completed(configuration.isCompleted())
                .comment(configuration.getComment())
                .version(configuration.getVersion())
                .elements(excludeElements
                        ? null
                        : toElementDtos(configuration.getElements())
                )
                .build();
    }

    public Collection<ConfigurationElementDto> toElementDtos(Collection<ConfigurationElement> configurationElements) {
        return configurationElements
                .stream()
                .map(this::toElementDto)
                .toList();
    }

    public ConfigurationElementDto toElementDto(ConfigurationElement configurationElement) {
        return ConfigurationElementDto
                .builder()
                .key(configurationElement.getKey())
                .elements(toElementDtos(configurationElement.getElements()))
                .fieldConfigurations(toFieldConfigurationDtos(configurationElement.getFieldConfigurations()))
                .collectionFieldConfigurations(toCollectionFieldConfigurationDtos(configurationElement.getCollectionFieldConfigurations()))
                .build();
    }

    private Collection<FieldConfigurationDto> toFieldConfigurationDtos(Collection<FieldConfiguration> fieldConfigurations) {
        return fieldConfigurations
                .stream()
                .map(this::toFieldConfigurationDto)
                .toList();
    }

    private FieldConfigurationDto toFieldConfigurationDto(FieldConfiguration fieldConfiguration) {
        return FieldConfigurationDto
                .builder()
                .key(fieldConfiguration.getKey())
                .type(fieldConfiguration.getType())
                .value(fieldConfiguration.getValue())
                .build();
    }

    private Collection<CollectionFieldConfigurationDto> toCollectionFieldConfigurationDtos(Collection<CollectionFieldConfiguration> collectionFieldConfigurations) {
        return collectionFieldConfigurations
                .stream().map(this::toCollectionFieldConfigurationDto)
                .toList();
    }

    private CollectionFieldConfigurationDto toCollectionFieldConfigurationDto(CollectionFieldConfiguration collectionFieldConfiguration) {
        return CollectionFieldConfigurationDto
                .builder()
                .key(collectionFieldConfiguration.getKey())
                .type(collectionFieldConfiguration.getType())
                .values(collectionFieldConfiguration.getValues())
                .build();
    }

}
