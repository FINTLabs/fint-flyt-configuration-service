package no.fintlabs.integration


import no.fintlabs.integration.model.configuration.dtos.CollectionFieldConfigurationDto
import no.fintlabs.integration.model.configuration.dtos.ConfigurationDto
import no.fintlabs.integration.model.configuration.dtos.ConfigurationElementDto
import no.fintlabs.integration.model.configuration.dtos.FieldConfigurationDto
import no.fintlabs.integration.model.configuration.entities.CollectionFieldConfiguration
import no.fintlabs.integration.model.configuration.entities.Configuration
import no.fintlabs.integration.model.configuration.entities.ConfigurationElement
import no.fintlabs.integration.model.configuration.entities.FieldConfiguration
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
                .id(0)
                .integrationId(1)
                .integrationMetadataId(2)
                .elements(List.of(
                        ConfigurationElement
                                .builder()
                                .id(0)
                                .key("element1")
                                .fieldConfigurations(List.of())
                                .collectionFieldConfigurations(List.of())
                                .elements(List.of())
                                .build(),
                        ConfigurationElement
                                .builder()
                                .id(1)
                                .key("element2")
                                .fieldConfigurations(List.of())
                                .collectionFieldConfigurations(List.of())
                                .elements(List.of(
                                        ConfigurationElement
                                                .builder()
                                                .id(2)
                                                .key("element21")
                                                .fieldConfigurations(List.of(
                                                        FieldConfiguration
                                                                .builder()
                                                                .id(0)
                                                                .key("field211")
                                                                .type(FieldConfiguration.Type.BOOLEAN)
                                                                .value("true")
                                                                .build(),
                                                        FieldConfiguration
                                                                .builder()
                                                                .id(1)
                                                                .key("field212")
                                                                .type(FieldConfiguration.Type.STRING)
                                                                .value("text")
                                                                .build(),
                                                ))
                                                .collectionFieldConfigurations(List.of(
                                                        CollectionFieldConfiguration
                                                                .builder()
                                                                .id(0)
                                                                .key("collectionField211")
                                                                .type(CollectionFieldConfiguration.Type.URL)
                                                                .values(List.of("www.example.com"))
                                                                .build(),
                                                        CollectionFieldConfiguration
                                                                .builder()
                                                                .id(1)
                                                                .key("collectionField212")
                                                                .type(CollectionFieldConfiguration.Type.STRING)
                                                                .values(List.of("text1", "text2"))
                                                                .build()
                                                ))
                                                .elements(List.of(
                                                        ConfigurationElement
                                                                .builder()
                                                                .id(3)
                                                                .key("element211")
                                                                .fieldConfigurations(List.of())
                                                                .collectionFieldConfigurations(List.of())
                                                                .elements(List.of())
                                                                .build()
                                                ))
                                                .build(),
                                        ConfigurationElement
                                                .builder()
                                                .id(4)
                                                .key("element22")
                                                .fieldConfigurations(List.of(
                                                        FieldConfiguration
                                                                .builder()
                                                                .id(2)
                                                                .key("field221")
                                                                .type(FieldConfiguration.Type.STRING)
                                                                .value(null)
                                                                .build()
                                                ))
                                                .collectionFieldConfigurations(List.of())
                                                .elements(List.of())
                                                .build()
                                ))
                                .build()
                ))
                .build()
    }

    ConfigurationDto createConfigurationDto() {
        return ConfigurationDto
                .builder()
                .integrationId(1)
                .integrationMetadataId(2)
                .elements(List.of(
                        ConfigurationElementDto
                                .builder()
                                .key("element1")
                                .fieldConfigurations(List.of())
                                .collectionFieldConfigurations(List.of())
                                .elements(List.of())
                                .build(),
                        ConfigurationElementDto
                                .builder()
                                .key("element2")
                                .fieldConfigurations(List.of())
                                .collectionFieldConfigurations(List.of())
                                .elements(List.of(
                                        ConfigurationElementDto
                                                .builder()
                                                .key("element21")
                                                .fieldConfigurations(List.of(
                                                        FieldConfigurationDto
                                                                .builder()
                                                                .key("field211")
                                                                .type(FieldConfiguration.Type.BOOLEAN)
                                                                .value("true")
                                                                .build(),
                                                        FieldConfigurationDto
                                                                .builder()
                                                                .key("field212")
                                                                .type(FieldConfiguration.Type.STRING)
                                                                .value("text")
                                                                .build(),
                                                ))
                                                .collectionFieldConfigurations(List.of(
                                                        CollectionFieldConfigurationDto
                                                                .builder()
                                                                .key("collectionField211")
                                                                .type(CollectionFieldConfiguration.Type.URL)
                                                                .values(List.of("www.example.com"))
                                                                .build(),
                                                        CollectionFieldConfigurationDto
                                                                .builder()
                                                                .key("collectionField212")
                                                                .type(CollectionFieldConfiguration.Type.STRING)
                                                                .values(List.of("text1", "text2"))
                                                                .build()
                                                ))
                                                .elements(List.of(
                                                        ConfigurationElementDto
                                                                .builder()
                                                                .key("element211")
                                                                .fieldConfigurations(List.of())
                                                                .collectionFieldConfigurations(List.of())
                                                                .elements(List.of())
                                                                .build()
                                                ))
                                                .build(),
                                        ConfigurationElementDto
                                                .builder()
                                                .key("element22")
                                                .fieldConfigurations(List.of(
                                                        FieldConfigurationDto
                                                                .builder()
                                                                .key("field221")
                                                                .type(FieldConfiguration.Type.STRING)
                                                                .value(null)
                                                                .build()
                                                ))
                                                .collectionFieldConfigurations(List.of())
                                                .elements(List.of())
                                                .build()
                                ))
                                .build()
                ))
                .build()
    }

    def 'should keep all values when mapping to dto with elements'() {
        when:
        ConfigurationDto result = configurationMappingService.toConfigurationDto(configuration, true)

        then:
        result == configurationDto
    }

    def 'should keep all values when mapping to dto without elements'() {
        when:
        ConfigurationDto result = configurationMappingService.toConfigurationDto(configuration)

        then:
        result == ConfigurationDto
                .builder()
                .integrationId(1)
                .integrationMetadataId(2)
                .build()
    }

    def 'should keep all values when mapping to configuration and then back to dto'() {
        when:
        Configuration firstResult = configurationMappingService.toConfiguration(configurationDto)
        ConfigurationDto secondResult = configurationMappingService.toConfigurationDto(firstResult, true)

        then:
        secondResult == configurationDto
    }

}
