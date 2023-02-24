package no.fintlabs


import no.fintlabs.model.configuration.dtos.ConfigurationDto
import no.fintlabs.model.configuration.dtos.ObjectMappingDto
import no.fintlabs.model.configuration.dtos.ValueMappingDto
import no.fintlabs.model.configuration.entities.Configuration
import no.fintlabs.model.configuration.entities.ObjectMapping
import no.fintlabs.model.configuration.entities.ValueMapping
import spock.lang.Specification

class ConfigurationMappingServiceSpec extends Specification {

    ConfigurationMappingService configurationMappingService
    Configuration configuration
    ConfigurationDto configurationDto

    def setup() {
        configurationMappingService = new ConfigurationMappingService()
        configuration = createConfiguration()
        configurationDto = createConfigurationDto()
    }

    Configuration createConfiguration() {
        return Configuration
                .builder()
                .id(1)
                .integrationId(1)
                .integrationMetadataId(2)
                .mapping(
                        ObjectMapping
                                .builder()
                                .valueMappingPerKey(Map.of())
                                .objectMappingPerKey(Map.of(
                                        "objectKey1",
                                        ObjectMapping
                                                .builder()
                                                .id(0)
                                                .valueMappingPerKey(Map.of())
                                                .objectMappingPerKey(Map.of())
                                                .objectCollectionMappingPerKey(Map.of())
                                                .build(),
                                        "objectKey2",
                                        ObjectMapping
                                                .builder()
                                                .id(1)
                                                .valueMappingPerKey(Map.of())
                                                .objectMappingPerKey(Map.of(
                                                        "objectKey21",
                                                        ObjectMapping
                                                                .builder()
                                                                .id(2)
                                                                .valueMappingPerKey(Map.of(
                                                                        "valueKey211",
                                                                        ValueMapping
                                                                                .builder()
                                                                                .id(0)
                                                                                .type(ValueMapping.Type.BOOLEAN)
                                                                                .mappingString("true")
                                                                                .build(),
                                                                        "valueKey212",
                                                                        ValueMapping
                                                                                .builder()
                                                                                .id(1)
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString("text")
                                                                                .build(),
                                                                ))
                                                                .objectMappingPerKey(Map.of(
                                                                        "objectKey211",
                                                                        ObjectMapping
                                                                                .builder()
                                                                                .id(3)
                                                                                .valueMappingPerKey(Map.of())
                                                                                .objectMappingPerKey(Map.of())
                                                                                .objectCollectionMappingPerKey(Map.of())
                                                                                .build()
                                                                ))
                                                                .objectCollectionMappingPerKey(Map.of())
                                                                .build(),
                                                        "objectKey22",
                                                        ObjectMapping
                                                                .builder()
                                                                .id(4)
                                                                .valueMappingPerKey(Map.of(
                                                                        "valueKey221",
                                                                        ValueMapping
                                                                                .builder()
                                                                                .id(2)
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString(null)
                                                                                .build()
                                                                ))
                                                                .objectMappingPerKey(Map.of())
                                                                .objectCollectionMappingPerKey(Map.of())
                                                                .build()
                                                ))
                                                .objectCollectionMappingPerKey(Map.of())
                                                .build()
                                ))
                                .objectCollectionMappingPerKey(Map.of())
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
                                .objectMappingPerKey(Map.of(
                                        "objectKey1",
                                        ObjectMappingDto
                                                .builder()
                                                .valueMappingPerKey(Map.of())
                                                .objectMappingPerKey(Map.of())
                                                .objectCollectionMappingPerKey(Map.of())
                                                .build(),
                                        "objectKey2",
                                        ObjectMappingDto
                                                .builder()
                                                .valueMappingPerKey(Map.of())
                                                .objectMappingPerKey(Map.of(
                                                        "objectKey21",
                                                        ObjectMappingDto
                                                                .builder()
                                                                .valueMappingPerKey(Map.of(
                                                                        "valueKey211",
                                                                        ValueMappingDto
                                                                                .builder()
                                                                                .type(ValueMapping.Type.BOOLEAN)
                                                                                .mappingString("true")
                                                                                .build(),
                                                                        "valueKey212",
                                                                        ValueMappingDto
                                                                                .builder()
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString("text")
                                                                                .build(),
                                                                ))
                                                                .objectMappingPerKey(Map.of(
                                                                        "objectKey211",
                                                                        ObjectMappingDto
                                                                                .builder()
                                                                                .valueMappingPerKey(Map.of())
                                                                                .objectMappingPerKey(Map.of())
                                                                                .objectCollectionMappingPerKey(Map.of())
                                                                                .build()
                                                                ))
                                                                .objectCollectionMappingPerKey(Map.of())
                                                                .build(),
                                                        "objectKey22",
                                                        ObjectMappingDto
                                                                .builder()
                                                                .valueMappingPerKey(Map.of(
                                                                        "valueKey221",
                                                                        ValueMappingDto
                                                                                .builder()
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString(null)
                                                                                .build()
                                                                ))
                                                                .objectMappingPerKey(Map.of())
                                                                .objectCollectionMappingPerKey(Map.of())
                                                                .build()
                                                ))
                                                .objectCollectionMappingPerKey(Map.of())
                                                .build()
                                ))
                                .objectCollectionMappingPerKey(Map.of())
                                .build()
                )
                .build()
    }

    def 'should keep all values when mapping to dto with objects'() {
        when:
        ConfigurationDto result = configurationMappingService.toConfigurationDto(configuration, false)

        then:
        result == configurationDto.toBuilder().id(1).build()
    }

    def 'should keep all values when mapping to dto without objects'() {
        when:
        ConfigurationDto result = configurationMappingService.toConfigurationDto(configuration, true)

        then:
        result == ConfigurationDto
                .builder()
                .id(1)
                .integrationId(1)
                .integrationMetadataId(2)
                .build()
    }

    def 'should keep all values when mapping to configuration and then back to dto'() {
        when:
        Configuration firstResult = configurationMappingService.toConfiguration(configurationDto)
        ConfigurationDto secondResult = configurationMappingService.toConfigurationDto(firstResult, false)

        then:
        secondResult == configurationDto.toBuilder().id(null).build()
    }

}
