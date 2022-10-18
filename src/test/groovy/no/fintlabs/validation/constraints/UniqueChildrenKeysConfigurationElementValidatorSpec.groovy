package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.dtos.CollectionFieldConfigurationDto
import no.fintlabs.model.configuration.dtos.ConfigurationElementDto
import no.fintlabs.model.configuration.dtos.FieldConfigurationDto
import no.fintlabs.model.configuration.entities.CollectionFieldConfiguration
import no.fintlabs.model.configuration.entities.ConfigurationElement
import no.fintlabs.model.configuration.entities.FieldConfiguration
import spock.lang.Specification

class UniqueChildrenKeysConfigurationElementValidatorSpec extends Specification {

    UniqueChildrenKeysConfigurationElementValidator uniqueChildrenKeysConfigurationElementValidator

    def setup() {
        uniqueChildrenKeysConfigurationElementValidator = new UniqueChildrenKeysConfigurationElementValidator()
    }

    private FieldConfiguration mockFieldConfiguration(String key) {
        FieldConfiguration fieldConfiguration = Mock(FieldConfiguration.class)
        fieldConfiguration.getKey() >> key
        return fieldConfiguration
    }

    private CollectionFieldConfiguration mockCollectionFieldConfiguration(String key) {
        CollectionFieldConfiguration collectionFieldConfiguration = Mock(CollectionFieldConfiguration.class)
        collectionFieldConfiguration.getKey() >> key
        return collectionFieldConfiguration
    }

    private ConfigurationElementDto mockConfigurationElementDto(
            String key,
            Collection<ConfigurationElement> elements,
            Collection<FieldConfiguration> fieldConfigurations,
            Collection<CollectionFieldConfiguration> collectionFieldConfigurations
    ) {
        ConfigurationElementDto configurationElement = Mock(ConfigurationElementDto.class)
        configurationElement.getKey() >> key
        configurationElement.getElements() >> elements
        configurationElement.getFieldConfigurations() >> fieldConfigurations
        configurationElement.getCollectionFieldConfigurations() >> collectionFieldConfigurations
        return configurationElement
    }

    def 'should return duplicate keys in a single collection and across child collections'() {
        given:
        ConfigurationElementDto configurationElementDto = ConfigurationElementDto
                .builder()
                .key("one")
                .fieldConfigurations(
                        List.of(
                                FieldConfigurationDto.builder().key("one").build(),
                                FieldConfigurationDto.builder().key("two").build(),
                                FieldConfigurationDto.builder().key("six").build(),
                                FieldConfigurationDto.builder().key("six").build()
                        )
                )
                .collectionFieldConfigurations(
                        List.of(
                                CollectionFieldConfigurationDto.builder().key("two").build(),
                                CollectionFieldConfigurationDto.builder().key("three").build(),
                                CollectionFieldConfigurationDto.builder().key("three").build(),
                                CollectionFieldConfigurationDto.builder().key("four").build()
                        )
                )
                .elements(
                        List.of(
                                ConfigurationElementDto.builder().key("five").build(),
                                ConfigurationElementDto.builder().key("four").build(),
                                ConfigurationElementDto.builder()
                                        .key("five")
                                        .fieldConfigurations(List.of(
                                                FieldConfigurationDto.builder().key("one").build()
                                        ))
                                        .build(),
                        )
                )
                .build()

        when:
        List<String> duplicateKeys = uniqueChildrenKeysConfigurationElementValidator.findDuplicateKeys(configurationElementDto)

        then:
        duplicateKeys.size() == 5
        duplicateKeys.containsAll("two", "three", "four", "five", "six")
    }

}
