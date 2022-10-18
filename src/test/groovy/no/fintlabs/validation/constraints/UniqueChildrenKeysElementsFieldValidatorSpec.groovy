package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.dtos.ConfigurationElementDto
import no.fintlabs.model.configuration.entities.ConfigurationElement
import spock.lang.Specification

class UniqueChildrenKeysElementsFieldValidatorSpec extends Specification {

    UniqueChildrenKeysElementsFieldValidator uniqueChildrenKeysElementsFieldValidator

    def setup() {
        uniqueChildrenKeysElementsFieldValidator = new UniqueChildrenKeysElementsFieldValidator()
    }

    private ConfigurationElement mockConfigurationElement(String key) {
        ConfigurationElement configurationElement = Mock(ConfigurationElement.class)
        configurationElement.getKey() >> key
        return configurationElement
    }

    def 'should return duplicate keys in a single collection'() {
        given:
        Collection<ConfigurationElementDto> configurationElements = List.of(
                ConfigurationElementDto.builder().key("one").build(),
                ConfigurationElementDto.builder().key("two").build(),
                ConfigurationElementDto.builder().key("two").build()
        )

        when:
        List<String> duplicateKeys = uniqueChildrenKeysElementsFieldValidator.findDuplicateKeys(configurationElements)

        then:
        duplicateKeys.size() == 1
        duplicateKeys.containsAll("two")
    }

}
