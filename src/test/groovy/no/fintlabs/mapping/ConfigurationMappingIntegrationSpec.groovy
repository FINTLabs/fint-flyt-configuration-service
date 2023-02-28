package no.fintlabs.mapping


import no.fintlabs.model.configuration.dtos.*
import no.fintlabs.model.configuration.entities.Configuration
import no.fintlabs.model.configuration.entities.ObjectMapping
import no.fintlabs.model.configuration.entities.ValueMapping
import no.fintlabs.model.configuration.entities.collection.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = [
        FromCollectionMappingMappingService.class,
        CollectionMappingMappingService.class,
        InstanceCollectionReferencesMappingService.class,
        PerKeyMappingService.class,
        ValueMappingMappingService.class,
        ValueCollectionMappingMappingService.class,
        ObjectMappingMappingService.class,
        ObjectCollectionMappingMappingService.class,
        ConfigurationMappingService.class
])
class ConfigurationMappingIntegrationSpec extends Specification {

    @Autowired
    ConfigurationMappingService configurationMappingService
    Configuration configuration
    ConfigurationDto configurationDto

    def setup() {
        configuration = createConfiguration()
        configurationDto = createConfigurationDto()
    }

    Configuration createConfiguration() {
        return Configuration
                .builder()
                .integrationId(1)
                .integrationMetadataId(2)
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
                                                                                .build(),
                                                                ))
                                                                .valueCollectionMappingPerKey(Map.of(
                                                                        "object2object1valueCollection1",
                                                                        ValueCollectionMapping
                                                                                .builder()
                                                                                .elementMappings(List.of(
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
                                                                                .fromCollectionMappings(List.of(
                                                                                        ValuesFromCollectionMapping
                                                                                                .builder()
                                                                                                .instanceCollectionReferencesOrdered(List.of(
                                                                                                        InstanceCollectionReference
                                                                                                                .builder()
                                                                                                                .index(0)
                                                                                                                .reference("reference1")
                                                                                                                .build()
                                                                                                ))
                                                                                                .elementMapping(
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
                                                                                                .elementMapping(
                                                                                                        ValueMapping
                                                                                                                .builder()
                                                                                                                .type(ValueMapping.Type.STRING)
                                                                                                                .mappingString("text3")
                                                                                                                .build()
                                                                                                )
                                                                                                .build(),
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
                                                .elementMappings(List.of())
                                                .fromCollectionMappings(List.of(
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
                                                                .elementMapping(
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
                .build()
    }

    ConfigurationDto createConfigurationDto() {
        return ConfigurationDto
                .builder()
                .integrationId(1)
                .integrationMetadataId(2)
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
                                                                                .build(),
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
                                                                                .build(),
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
                .build()
    }

    def 'should keep all values when mapping to dto with mapping'() {
        when:
        ConfigurationDto result = configurationMappingService.toDto(configuration, false)

        then:
        result == configurationDto
    }

    def 'should keep all values when mapping to dto without mapping'() {
        when:
        ConfigurationDto result = configurationMappingService.toDto(configuration, true)

        then:
        result == ConfigurationDto
                .builder()
                .integrationId(1)
                .integrationMetadataId(2)
                .build()
    }

    def 'should keep all values when mapping to configuration and then back to dto'() {
        when:
        Configuration firstResult = configurationMappingService.toEntity(configurationDto)
        ConfigurationDto secondResult = configurationMappingService.toDto(firstResult, false)

        then:
        secondResult == configurationDto
    }

}
