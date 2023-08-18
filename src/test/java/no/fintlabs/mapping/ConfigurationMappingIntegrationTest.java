package no.fintlabs.mapping;

import no.fintlabs.model.configuration.dtos.*;
import no.fintlabs.model.configuration.entities.Configuration;
import no.fintlabs.model.configuration.entities.ObjectMapping;
import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.model.configuration.entities.collection.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {
        ObjectsFromCollectionMappingMappingService.class,
        ObjectCollectionMappingMappingService.class,
        ValuesFromCollectionMappingMappingService.class,
        ValueCollectionMappingMappingService.class,
        InstanceCollectionReferencesMappingService.class,
        PerKeyMappingService.class,
        ValueMappingMappingService.class,
        ValueCollectionMappingMappingService.class,
        ObjectMappingMappingService.class,
        ObjectCollectionMappingMappingService.class,
        ConfigurationMappingService.class
})
public class ConfigurationMappingIntegrationTest {

    @Autowired
    ConfigurationMappingService configurationMappingService;
    Configuration configuration;
    ConfigurationDto configurationDto;

    @BeforeEach
    public void setup() {
        configuration = createConfiguration();
        configurationDto = createConfigurationDto();
    }

    private Configuration createConfiguration() {
        return Configuration
                .builder()
                .integrationId(1L)
                .integrationMetadataId(2L)
                .mapping(
                        ObjectMapping
                                .builder()
                                .valueMappingPerKey(Map.of())
                                .valueCollectionMappingPerKey(Map.of())
                                .objectMappingPerKey(Map.of(
                                        "object1",
                                        ObjectMapping
                                                .builder()
                                                .valueMappingPerKey(Map.of())
                                                .valueCollectionMappingPerKey(Map.of())
                                                .objectMappingPerKey(Map.of())
                                                .objectCollectionMappingPerKey(Map.of())
                                                .build(),
                                        "object2",
                                        ObjectMapping
                                                .builder()
                                                .valueMappingPerKey(Map.of())
                                                .valueCollectionMappingPerKey(Map.of())
                                                .objectMappingPerKey(Map.of(
                                                        "object2object1",
                                                        ObjectMapping
                                                                .builder()
                                                                .valueMappingPerKey(Map.of(
                                                                        "object2object1value1",
                                                                        ValueMapping
                                                                                .builder()
                                                                                .type(ValueMapping.Type.BOOLEAN)
                                                                                .mappingString("true")
                                                                                .build(),
                                                                        "object2object1value2",
                                                                        ValueMapping
                                                                                .builder()
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString("text1")
                                                                                .build()
                                                                        ))
                                                                .valueCollectionMappingPerKey(Map.of(
                                                                        "object2object1valueCollection1",
                                                                        ValueCollectionMapping
                                                                                .builder()
                                                                                .valueMappings(List.of(
                                                                                        ValueMapping
                                                                                                .builder()
                                                                                                .type(ValueMapping.Type.STRING)
                                                                                                .mappingString("text2")
                                                                                                .build(),
                                                                                        ValueMapping
                                                                                                .builder()
                                                                                                .type(ValueMapping.Type.DYNAMIC_STRING)
                                                                                                .mappingString("dynamicString1")
                                                                                                .build()
                                                                                ))
                                                                                .valuesFromCollectionMappings(List.of(
                                                                                        ValuesFromCollectionMapping
                                                                                                .builder()
                                                                                                .instanceCollectionReferencesOrdered(List.of(
                                                                                                        InstanceCollectionReference
                                                                                                                .builder()
                                                                                                                .index(0)
                                                                                                                .reference("reference1")
                                                                                                                .build()
                                                                                                ))
                                                                                                .valueMapping(
                                                                                                        ValueMapping
                                                                                                                .builder()
                                                                                                                .type(ValueMapping.Type.DYNAMIC_STRING)
                                                                                                                .mappingString("dynamicString2")
                                                                                                                .build()
                                                                                                )
                                                                                                .build(),
                                                                                        ValuesFromCollectionMapping
                                                                                                .builder()
                                                                                                .instanceCollectionReferencesOrdered(List.of(
                                                                                                        InstanceCollectionReference
                                                                                                                .builder()
                                                                                                                .index(0)
                                                                                                                .reference("reference2")
                                                                                                                .build(),
                                                                                                        InstanceCollectionReference
                                                                                                                .builder()
                                                                                                                .index(1)
                                                                                                                .reference("reference3")
                                                                                                                .build()
                                                                                                ))
                                                                                                .valueMapping(
                                                                                                        ValueMapping
                                                                                                                .builder()
                                                                                                                .type(ValueMapping.Type.STRING)
                                                                                                                .mappingString("text3")
                                                                                                                .build()
                                                                                                )
                                                                                                .build()
                                                                                        ))
                                                                                .build()
                                                                ))
                                                                .objectMappingPerKey(Map.of(
                                                                        "object2object1object1",
                                                                        ObjectMapping
                                                                                .builder()
                                                                                .valueMappingPerKey(Map.of())
                                                                                .valueCollectionMappingPerKey(Map.of())
                                                                                .objectMappingPerKey(Map.of())
                                                                                .objectCollectionMappingPerKey(Map.of())
                                                                                .build()
                                                                ))
                                                                .objectCollectionMappingPerKey(Map.of())
                                                                .build(),
                                                        "object2object2",
                                                        ObjectMapping
                                                                .builder()
                                                                .valueMappingPerKey(Map.of(
                                                                        "object2object2value1",
                                                                        ValueMapping
                                                                                .builder()
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString(null)
                                                                                .build()
                                                                ))
                                                                .valueCollectionMappingPerKey(Map.of())
                                                                .objectMappingPerKey(Map.of())
                                                                .objectCollectionMappingPerKey(Map.of())
                                                                .build()
                                                ))
                                                .objectCollectionMappingPerKey(Map.of())
                                                .build()
                                ))
                                .objectCollectionMappingPerKey(Map.of(
                                        "objectCollection1",
                                        ObjectCollectionMapping
                                                .builder()
                                                .objectMappings(List.of())
                                                .objectsFromCollectionMappings(List.of(
                                                        ObjectsFromCollectionMapping
                                                                .builder()
                                                                .instanceCollectionReferencesOrdered(List.of(
                                                                        InstanceCollectionReference
                                                                                .builder()
                                                                                .index(1)
                                                                                .reference("reference4")
                                                                                .build(),
                                                                        InstanceCollectionReference
                                                                                .builder()
                                                                                .index(0)
                                                                                .reference("reference5")
                                                                                .build()
                                                                ))
                                                                .objectMapping(
                                                                        ObjectMapping
                                                                                .builder()
                                                                                .valueMappingPerKey(Map.of(
                                                                                        "objectCollection1value1",
                                                                                        ValueMapping
                                                                                                .builder()
                                                                                                .type(ValueMapping.Type.FILE)
                                                                                                .mappingString("fileReference1")
                                                                                                .build()
                                                                                ))
                                                                                .valueCollectionMappingPerKey(Map.of())
                                                                                .objectMappingPerKey(Map.of())
                                                                                .objectCollectionMappingPerKey(Map.of())
                                                                                .build()
                                                                )
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .build()
                )
                .build();
    }

    private ConfigurationDto createConfigurationDto() {
        return ConfigurationDto
                .builder()
                .integrationId(1L)
                .integrationMetadataId(2L)
                .mapping(
                        ObjectMappingDto
                                .builder()
                                .valueMappingPerKey(Map.of())
                                .valueCollectionMappingPerKey(Map.of())
                                .objectMappingPerKey(Map.of(
                                        "object1",
                                        ObjectMappingDto
                                                .builder()
                                                .valueMappingPerKey(Map.of())
                                                .valueCollectionMappingPerKey(Map.of())
                                                .objectMappingPerKey(Map.of())
                                                .objectCollectionMappingPerKey(Map.of())
                                                .build(),
                                        "object2",
                                        ObjectMappingDto
                                                .builder()
                                                .valueMappingPerKey(Map.of())
                                                .valueCollectionMappingPerKey(Map.of())
                                                .objectMappingPerKey(Map.of(
                                                        "object2object1",
                                                        ObjectMappingDto
                                                                .builder()
                                                                .valueMappingPerKey(Map.of(
                                                                        "object2object1value1",
                                                                        ValueMappingDto
                                                                                .builder()
                                                                                .type(ValueMapping.Type.BOOLEAN)
                                                                                .mappingString("true")
                                                                                .build(),
                                                                        "object2object1value2",
                                                                        ValueMappingDto
                                                                                .builder()
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString("text1")
                                                                                .build()
                                                                        ))
                                                                .valueCollectionMappingPerKey(Map.of(
                                                                        "object2object1valueCollection1",
                                                                        CollectionMappingDto
                                                                                .<ValueMappingDto> builder()
                                                                                .elementMappings(List.of(
                                                                                        ValueMappingDto
                                                                                                .builder()
                                                                                                .type(ValueMapping.Type.STRING)
                                                                                                .mappingString("text2")
                                                                                                .build(),
                                                                                        ValueMappingDto
                                                                                                .builder()
                                                                                                .type(ValueMapping.Type.DYNAMIC_STRING)
                                                                                                .mappingString("dynamicString1")
                                                                                                .build()
                                                                                ))
                                                                                .fromCollectionMappings(List.of(
                                                                                        FromCollectionMappingDto
                                                                                                .<ValueMappingDto> builder()
                                                                                                .instanceCollectionReferencesOrdered(List.of(
                                                                                                        "reference1"
                                                                                                ))
                                                                                                .elementMapping(
                                                                                                        ValueMappingDto
                                                                                                                .builder()
                                                                                                                .type(ValueMapping.Type.DYNAMIC_STRING)
                                                                                                                .mappingString("dynamicString2")
                                                                                                                .build()
                                                                                                )
                                                                                                .build(),
                                                                                        FromCollectionMappingDto
                                                                                                .<ValueMappingDto> builder()
                                                                                                .instanceCollectionReferencesOrdered(List.of(
                                                                                                        "reference2",
                                                                                                        "reference3"
                                                                                                ))
                                                                                                .elementMapping(
                                                                                                        ValueMappingDto
                                                                                                                .builder()
                                                                                                                .type(ValueMapping.Type.STRING)
                                                                                                                .mappingString("text3")
                                                                                                                .build()
                                                                                                )
                                                                                                .build()
                                                                                ))
                                                                                .build()
                                                                        ))
                                                                .objectMappingPerKey(Map.of(
                                                                        "object2object1object1",
                                                                        ObjectMappingDto
                                                                                .builder()
                                                                                .valueMappingPerKey(Map.of())
                                                                                .valueCollectionMappingPerKey(Map.of())
                                                                                .objectMappingPerKey(Map.of())
                                                                                .objectCollectionMappingPerKey(Map.of())
                                                                                .build()
                                                                ))
                                                                .objectCollectionMappingPerKey(Map.of())
                                                                .build(),
                                                        "object2object2",
                                                        ObjectMappingDto
                                                                .builder()
                                                                .valueMappingPerKey(Map.of(
                                                                        "object2object2value1",
                                                                        ValueMappingDto
                                                                                .builder()
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString(null)
                                                                                .build()
                                                                ))
                                                                .valueCollectionMappingPerKey(Map.of())
                                                                .objectMappingPerKey(Map.of())
                                                                .objectCollectionMappingPerKey(Map.of())
                                                                .build()
                                                ))
                                                .objectCollectionMappingPerKey(Map.of())
                                                .build()
                                ))
                                .objectCollectionMappingPerKey(Map.of(
                                        "objectCollection1",
                                        CollectionMappingDto
                                                .<ObjectMappingDto> builder()
                                                .elementMappings(List.of())
                                                .fromCollectionMappings(List.of(
                                                        FromCollectionMappingDto
                                                                .<ObjectMappingDto> builder()
                                                                .instanceCollectionReferencesOrdered(List.of(
                                                                        "reference5",
                                                                        "reference4"
                                                                ))
                                                                .elementMapping(
                                                                        ObjectMappingDto
                                                                                .builder()
                                                                                .valueMappingPerKey(Map.of(
                                                                                        "objectCollection1value1",
                                                                                        ValueMappingDto
                                                                                                .builder()
                                                                                                .type(ValueMapping.Type.FILE)
                                                                                                .mappingString("fileReference1")
                                                                                                .build()
                                                                                ))
                                                                                .valueCollectionMappingPerKey(Map.of())
                                                                                .objectMappingPerKey(Map.of())
                                                                                .objectCollectionMappingPerKey(Map.of())
                                                                                .build()
                                                                )
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .build()
                )
                .build();
    }

    @Test
    public void shouldKeepAllValuesWhenMappingToDtoWithMapping() {
        ConfigurationDto result = configurationMappingService.toDto(configuration, false);
        assertEquals(configurationDto, result);
    }

    @Test
    public void shouldKeepAllValuesWhenMappingToDtoWithoutMapping() {
        ConfigurationDto result = configurationMappingService.toDto(configuration, true);
        assertEquals(
                ConfigurationDto.builder()
                        .integrationId(1L)
                        .integrationMetadataId(2L)
                        .build(),
                result
        );
    }

    @Test
    public void shouldKeepAllValuesWhenMappingToConfigurationAndThenBackToDto() {
        Configuration firstResult = configurationMappingService.toEntity(configurationDto);
        ConfigurationDto secondResult = configurationMappingService.toDto(firstResult, false);
        assertEquals(configurationDto, secondResult);
    }
}
