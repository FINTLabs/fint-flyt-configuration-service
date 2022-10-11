package no.fintlabs.integration.validation.constraints

import no.fintlabs.integration.model.ConfigurationElement
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
        Collection<ConfigurationElement> configurationElements = List.of(
                mockConfigurationElement("one"),
                mockConfigurationElement("two"),
                mockConfigurationElement("two"),
        )

        when:
        List<String> duplicateKeys = uniqueChildrenKeysElementsFieldValidator.findDuplicateKeys(configurationElements)

        then:
        duplicateKeys.size() == 1
        duplicateKeys.containsAll("two")
    }

}
