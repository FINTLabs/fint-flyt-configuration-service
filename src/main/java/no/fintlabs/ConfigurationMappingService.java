package no.fintlabs;

import no.fintlabs.model.configuration.dtos.*;
import no.fintlabs.model.configuration.entities.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
public class ConfigurationMappingService {

    private <T, R> Map<String, R> mapObjectPerKey(
            Map<String, T> existingMap,
            Function<T, R> mapping
    ) {
        return existingMap
                .keySet()
                .stream()
                .collect(toMap(
                        Function.identity(),
                        key -> mapping.apply(existingMap.get(key))
                ));
    }

    public Configuration toConfiguration(ConfigurationDto configurationDto) {
        return Configuration
                .builder()
                .integrationId(configurationDto.getIntegrationId())
                .integrationMetadataId(configurationDto.getIntegrationMetadataId())
                .comment(configurationDto.getComment())
                .completed(configurationDto.isCompleted())
                .mapping(toElementMapping(configurationDto.getMapping()))
                .build();
    }

    public ElementMapping toElementMapping(ElementMappingDto elementMappingDto) {
        return ElementMapping
                .builder()
                .valueMappingPerKey(
                        mapObjectPerKey(elementMappingDto.getValueMappingPerKey(), this::toValueMapping)
                )
                .elementMappingPerKey(
                        mapObjectPerKey(elementMappingDto.getElementMappingPerKey(), this::toElementMapping)
                )
                .elementCollectionMappingPerKey(
                        mapObjectPerKey(elementMappingDto.getElementCollectionMappingPerKey(), this::toElementCollectionMapping)
                )
                .build();
    }

    private ValueMapping toValueMapping(ValueMappingDto valueMappingDto) {
        return ValueMapping
                .builder()
                .type(valueMappingDto.getType())
                .mappingString(valueMappingDto.getMappingString())
                .build();
    }

    private ElementCollectionMapping toElementCollectionMapping(ElementCollectionMappingDto elementCollectionMappingDto) {
        return ElementCollectionMapping
                .builder()
                .elementMappings(
                        elementCollectionMappingDto
                                .getElementMappings()
                                .stream()
                                .map(this::toElementMapping)
                                .toList()
                )
                .elementsFromCollectionMappings(
                        elementCollectionMappingDto
                                .getElementsFromCollectionMappings()
                                .stream()
                                .map(this::toElementsFromCollectionMapping)
                                .toList()
                )
                .build();
    }

    private ElementsFromCollectionMapping toElementsFromCollectionMapping(
            ElementsFromCollectionMappingDto elementsFromCollectionMappingDto
    ) {
        return ElementsFromCollectionMapping
                .builder()
                .instanceCollectionReferencesOrdered(
                        new ArrayList<>(elementsFromCollectionMappingDto.getInstanceCollectionReferencesOrdered())
                )
                .elementMapping(toElementMapping(elementsFromCollectionMappingDto.getElementMapping()))
                .build();
    }

    public ConfigurationDto toConfigurationDto(Configuration configuration, boolean excludeMapping) {
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
                        : toElementMappingDto(configuration.getMapping())
                )
                .build();
    }

    private ValueMappingDto toValueMappingDto(ValueMapping valueMapping) {
        return ValueMappingDto
                .builder()
                .type(valueMapping.getType())
                .mappingString(valueMapping.getMappingString())
                .build();
    }

    private ElementMappingDto toElementMappingDto(ElementMapping elementMapping) {
        return ElementMappingDto
                .builder()
                .valueMappingPerKey(
                        mapObjectPerKey(elementMapping.getValueMappingPerKey(), this::toValueMappingDto)
                )
                .elementMappingPerKey(
                        mapObjectPerKey(elementMapping.getElementMappingPerKey(), this::toElementMappingDto)
                )
                .elementCollectionMappingPerKey(
                        mapObjectPerKey(elementMapping.getElementCollectionMappingPerKey(), this::toElementCollectionMappingDto)
                )
                .build();
    }

    private ElementCollectionMappingDto toElementCollectionMappingDto(ElementCollectionMapping elementCollectionMapping) {
        return ElementCollectionMappingDto
                .builder()
                .elementMappings(
                        elementCollectionMapping
                                .getElementMappings()
                                .stream()
                                .map(this::toElementMappingDto)
                                .toList()
                )
                .elementsFromCollectionMappings(
                        elementCollectionMapping
                                .getElementsFromCollectionMappings()
                                .stream()
                                .map(this::toElementsFromCollectionMappingDto)
                                .toList()
                )
                .build();
    }

    private ElementsFromCollectionMappingDto toElementsFromCollectionMappingDto(ElementsFromCollectionMapping elementsFromCollectionMapping) {
        return ElementsFromCollectionMappingDto
                .builder()
                .instanceCollectionReferencesOrdered(new ArrayList<>(elementsFromCollectionMapping.getInstanceCollectionReferencesOrdered()))
                .elementMapping(toElementMappingDto(elementsFromCollectionMapping.getElementMapping()))
                .build();
    }

}
