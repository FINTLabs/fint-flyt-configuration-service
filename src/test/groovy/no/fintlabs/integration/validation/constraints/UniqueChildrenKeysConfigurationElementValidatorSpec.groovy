package no.fintlabs.integration.validation.constraints

import no.fintlabs.integration.model.ConfigurationElement
import no.fintlabs.integration.model.FieldCollectionConfiguration
import no.fintlabs.integration.model.FieldConfiguration
import spock.lang.Specification

import static java.util.Collections.emptyList

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

    private FieldCollectionConfiguration mockFieldCollectionConfiguration(String key) {
        FieldCollectionConfiguration fieldCollectionConfiguration = Mock(FieldCollectionConfiguration.class)
        fieldCollectionConfiguration.getKey() >> key
        return fieldCollectionConfiguration
    }

    private ConfigurationElement mockConfigurationElement(
            String key,
            Collection<ConfigurationElement> elements,
            Collection<FieldConfiguration> fieldConfigurations,
            Collection<FieldCollectionConfiguration> fieldCollectionConfigurations
    ) {
        ConfigurationElement configurationElement = Mock(ConfigurationElement.class)
        configurationElement.getKey() >> key
        configurationElement.getElements() >> elements
        configurationElement.getFieldConfigurations() >> fieldConfigurations
        configurationElement.getFieldCollectionConfigurations() >> fieldCollectionConfigurations
        return configurationElement
    }

    def 'should return duplicate keys in a single collection and across child collections'() {
        given:
        ConfigurationElement configurationElement = mockConfigurationElement(
                "one",
                List.of(
                        mockConfigurationElement("five", emptyList(), emptyList(), emptyList()),
                        mockConfigurationElement("four", emptyList(), emptyList(), emptyList()),
                        mockConfigurationElement("five", emptyList(), List.of(mockFieldConfiguration("one")), emptyList())
                ),
                List.of(
                        mockFieldConfiguration("one"),
                        mockFieldConfiguration("two"),
                        mockFieldConfiguration("six"),
                        mockFieldConfiguration("six"),
                ),
                List.of(
                        mockFieldCollectionConfiguration("two"),
                        mockFieldCollectionConfiguration("three"),
                        mockFieldCollectionConfiguration("three"),
                        mockFieldCollectionConfiguration("four"),
                ),
        )

        when:
        List<String> duplicateKeys = uniqueChildrenKeysConfigurationElementValidator.findDuplicateKeys(configurationElement)

        then:
        duplicateKeys.size() == 5
        duplicateKeys.containsAll("two", "three", "four", "five", "six")
    }

}
