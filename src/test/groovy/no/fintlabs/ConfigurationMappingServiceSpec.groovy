package no.fintlabs

import no.fintlabs.model.configuration.dtos.ConfigurationDto
import no.fintlabs.model.configuration.dtos.ElementMappingDto
import no.fintlabs.model.configuration.dtos.ValueMappingDto
import no.fintlabs.model.configuration.entities.Configuration
import no.fintlabs.model.configuration.entities.ElementMapping
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
                        ElementMapping
                                .builder()
                                .valueMappingPerKey(Map.of())
                                .elementMappingPerKey(Map.of(
                                        "element1",
                                        ElementMapping
                                                .builder()
                                                .id(0)
                                                .valueMappingPerKey(Map.of())
                                                .elementMappingPerKey(Map.of())
                                                .elementCollectionMappingPerKey(Map.of())
                                                .build(),
                                        "element2",
                                        ElementMapping
                                                .builder()
                                                .id(1)
                                                .valueMappingPerKey(Map.of())
                                                .elementMappingPerKey(Map.of(
                                                        "element21",
                                                        ElementMapping
                                                                .builder()
                                                                .id(2)
                                                                .valueMappingPerKey(Map.of(
                                                                        "field211",
                                                                        ValueMapping
                                                                                .builder()
                                                                                .id(0)
                                                                                .type(ValueMapping.Type.BOOLEAN)
                                                                                .mappingString("true")
                                                                                .build(),
                                                                        "field212",
                                                                        ValueMapping
                                                                                .builder()
                                                                                .id(1)
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString("text")
                                                                                .build(),
                                                                ))
                                                                .elementMappingPerKey(Map.of(
                                                                        "element211",
                                                                        ElementMapping
                                                                                .builder()
                                                                                .id(3)
                                                                                .valueMappingPerKey(Map.of())
                                                                                .elementMappingPerKey(Map.of())
                                                                                .elementCollectionMappingPerKey(Map.of())
                                                                                .build()
                                                                ))
                                                                .elementCollectionMappingPerKey(Map.of())
                                                                .build(),
                                                        "element22",
                                                        ElementMapping
                                                                .builder()
                                                                .id(4)
                                                                .valueMappingPerKey(Map.of(
                                                                        "field221",
                                                                        ValueMapping
                                                                                .builder()
                                                                                .id(2)
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString(null)
                                                                                .build()
                                                                ))
                                                                .elementMappingPerKey(Map.of())
                                                                .elementCollectionMappingPerKey(Map.of())
                                                                .build()
                                                ))
                                                .elementCollectionMappingPerKey(Map.of())
                                                .build()
                                ))
                                .elementCollectionMappingPerKey(Map.of())
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
                        ElementMappingDto
                                .builder()
                                .valueMappingPerKey(Map.of())
                                .elementMappingPerKey(Map.of(
                                        "element1",
                                        ElementMappingDto
                                                .builder()
                                                .valueMappingPerKey(Map.of())
                                                .elementMappingPerKey(Map.of())
                                                .elementCollectionMappingPerKey(Map.of())
                                                .build(),
                                        "element2",
                                        ElementMappingDto
                                                .builder()
                                                .valueMappingPerKey(Map.of())
                                                .elementMappingPerKey(Map.of(
                                                        "element21",
                                                        ElementMappingDto
                                                                .builder()
                                                                .valueMappingPerKey(Map.of(
                                                                        "field211",
                                                                        ValueMappingDto
                                                                                .builder()
                                                                                .type(ValueMapping.Type.BOOLEAN)
                                                                                .mappingString("true")
                                                                                .build(),
                                                                        "field212",
                                                                        ValueMappingDto
                                                                                .builder()
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString("text")
                                                                                .build(),
                                                                ))
                                                                .elementMappingPerKey(Map.of(
                                                                        "element211",
                                                                        ElementMappingDto
                                                                                .builder()
                                                                                .valueMappingPerKey(Map.of())
                                                                                .elementMappingPerKey(Map.of())
                                                                                .elementCollectionMappingPerKey(Map.of())
                                                                                .build()
                                                                ))
                                                                .elementCollectionMappingPerKey(Map.of())
                                                                .build(),
                                                        "element22",
                                                        ElementMappingDto
                                                                .builder()
                                                                .valueMappingPerKey(Map.of(
                                                                        "field221",
                                                                        ValueMappingDto
                                                                                .builder()
                                                                                .type(ValueMapping.Type.STRING)
                                                                                .mappingString(null)
                                                                                .build()
                                                                ))
                                                                .elementMappingPerKey(Map.of())
                                                                .elementCollectionMappingPerKey(Map.of())
                                                                .build()
                                                ))
                                                .elementCollectionMappingPerKey(Map.of())
                                                .build()
                                ))
                                .elementCollectionMappingPerKey(Map.of())
                                .build()
                )
                .build()
    }

    def 'should keep all values when mapping to dto with elements'() {
        when:
        ConfigurationDto result = configurationMappingService.toConfigurationDto(configuration, false)

        then:
        result == configurationDto.toBuilder().id(1).build()
    }

    def 'should keep all values when mapping to dto without elements'() {
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
