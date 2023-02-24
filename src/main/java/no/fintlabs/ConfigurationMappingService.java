package no.fintlabs;

import no.fintlabs.model.configuration.dtos.*;
import no.fintlabs.model.configuration.entities.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

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
                .mapping(toObjectMapping(configurationDto.getMapping()))
                .build();
    }

    public ObjectMapping toObjectMapping(ObjectMappingDto objectMappingDto) {
        return ObjectMapping
                .builder()
                .valueMappingPerKey(
                        mapObjectPerKey(objectMappingDto.getValueMappingPerKey(), this::toValueMapping)
                )
                .objectMappingPerKey(
                        mapObjectPerKey(objectMappingDto.getObjectMappingPerKey(), this::toObjectMapping)
                )
                .objectCollectionMappingPerKey(
                        mapObjectPerKey(objectMappingDto.getObjectCollectionMappingPerKey(), this::toObjectCollectionMapping)
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

    private ObjectCollectionMapping toObjectCollectionMapping(ObjectCollectionMappingDto objectCollectionMappingDto) {
        return ObjectCollectionMapping
                .builder()
                .objectMappings(
                        objectCollectionMappingDto
                                .getObjectMappings()
                                .stream()
                                .map(this::toObjectMapping)
                                .toList()
                )
                .objectsFromCollectionMappings(
                        objectCollectionMappingDto
                                .getObjectsFromCollectionMappings()
                                .stream()
                                .map(this::toObjectsFromCollectionMapping)
                                .toList()
                )
                .build();
    }

    private ObjectsFromCollectionMapping toObjectsFromCollectionMapping(
            ObjectsFromCollectionMappingDto objectsFromCollectionMappingDto
    ) {
        return ObjectsFromCollectionMapping
                .builder()
                .instanceCollectionReferencesOrdered(
                        toInstanceCollectionReferencesOrdered(
                                objectsFromCollectionMappingDto.getInstanceCollectionReferencesOrdered()
                        )
                )
                .objectMapping(toObjectMapping(objectsFromCollectionMappingDto.getObjectMappingDto()))
                .build();
    }

    private List<InstanceCollectionReference> toInstanceCollectionReferencesOrdered(List<String> instanceCollectionReferencesOrderedFromDto) {
        return IntStream.range(0, instanceCollectionReferencesOrderedFromDto.size())
                .mapToObj(index -> InstanceCollectionReference
                        .builder()
                        .index(index)
                        .reference(instanceCollectionReferencesOrderedFromDto.get(index))
                        .build()
                )
                .toList();
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
                        : toObjectMappingDto(configuration.getMapping())
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

    private ObjectMappingDto toObjectMappingDto(ObjectMapping objectMapping) {
        return ObjectMappingDto
                .builder()
                .valueMappingPerKey(
                        mapObjectPerKey(objectMapping.getValueMappingPerKey(), this::toValueMappingDto)
                )
                .objectMappingPerKey(
                        mapObjectPerKey(objectMapping.getObjectMappingPerKey(), this::toObjectMappingDto)
                )
                .objectCollectionMappingPerKey(
                        mapObjectPerKey(objectMapping.getObjectCollectionMappingPerKey(), this::toObjectCollectionMappingDto)
                )
                .build();
    }

    private ObjectCollectionMappingDto toObjectCollectionMappingDto(ObjectCollectionMapping objectCollectionMapping) {
        return ObjectCollectionMappingDto
                .builder()
                .objectMappings(
                        objectCollectionMapping
                                .getObjectMappings()
                                .stream()
                                .map(this::toObjectMappingDto)
                                .toList()
                )
                .objectsFromCollectionMappings(
                        objectCollectionMapping
                                .getObjectsFromCollectionMappings()
                                .stream()
                                .map(this::toObjectsFromCollectionMappingDto)
                                .toList()
                )
                .build();
    }

    private ObjectsFromCollectionMappingDto toObjectsFromCollectionMappingDto(ObjectsFromCollectionMapping objectsFromCollectionMapping) {
        return ObjectsFromCollectionMappingDto
                .builder()
                .instanceCollectionReferencesOrdered(
                        objectsFromCollectionMapping.getInstanceCollectionReferencesOrdered()
                                .stream()
                                .sorted(Comparator.comparingInt(InstanceCollectionReference::getIndex))
                                .map(InstanceCollectionReference::getReference)
                                .toList()
                )
                .objectMappingDto(toObjectMappingDto(objectsFromCollectionMapping.getObjectMapping()))
                .build();
    }

}
