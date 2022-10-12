package no.fintlabs.integration.validation.constraints

import no.fintlabs.integration.model.configuration.ConfigurationElement
import no.fintlabs.integration.model.configuration.CollectionFieldConfiguration
import no.fintlabs.integration.model.configuration.FieldConfiguration
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

    private CollectionFieldConfiguration mockCollectionFieldConfiguration(String key) {
        CollectionFieldConfiguration collectionFieldConfiguration = Mock(CollectionFieldConfiguration.class)
        collectionFieldConfiguration.getKey() >> key
        return collectionFieldConfiguration
    }

    private ConfigurationElement mockConfigurationElement(
            String key,
            Collection<ConfigurationElement> elements,
            Collection<FieldConfiguration> fieldConfigurations,
            Collection<CollectionFieldConfiguration> collectionFieldConfigurations
    ) {
        ConfigurationElement configurationElement = Mock(ConfigurationElement.class)
        configurationElement.getKey() >> key
        configurationElement.getElements() >> elements
        configurationElement.getFieldConfigurations() >> fieldConfigurations
        configurationElement.getCollectionFieldConfigurations() >> collectionFieldConfigurations
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
                        mockCollectionFieldConfiguration("two"),
                        mockCollectionFieldConfiguration("three"),
                        mockCollectionFieldConfiguration("three"),
                        mockCollectionFieldConfiguration("four"),
                ),
        )

        when:
        List<String> duplicateKeys = uniqueChildrenKeysConfigurationElementValidator.findDuplicateKeys(configurationElement)

        then:
        duplicateKeys.size() == 5
        duplicateKeys.containsAll("two", "three", "four", "five", "six")
    }

}
