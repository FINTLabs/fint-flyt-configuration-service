package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.dtos.CollectionFieldConfigurationDto
import no.fintlabs.validation.parsability.CollectionFieldParsabilityValidator
import spock.lang.Specification

class ValueParsableAsTypeCollectionFieldConfigurationValidatorSpec extends Specification {

    CollectionFieldParsabilityValidator collectionFieldParsabilityValidator1
    CollectionFieldParsabilityValidator collectionFieldParsabilityValidator2
    ValueParsableAsTypeCollectionFieldConfigurationValidator valueParsableAsTypeCollectionFieldConfigurationValidator

    def setup() {
        collectionFieldParsabilityValidator1 = Mock(CollectionFieldParsabilityValidator.class)
        collectionFieldParsabilityValidator2 = Mock(CollectionFieldParsabilityValidator.class)
        valueParsableAsTypeCollectionFieldConfigurationValidator = new ValueParsableAsTypeCollectionFieldConfigurationValidator(
                List.of(collectionFieldParsabilityValidator1, collectionFieldParsabilityValidator2)
        )
    }

    def "should return true if all field parsability validators return true"() {
        given:
        collectionFieldParsabilityValidator1.isValid(_ as CollectionFieldConfigurationDto) >> true
        collectionFieldParsabilityValidator2.isValid(_ as CollectionFieldConfigurationDto) >> true

        when:
        boolean valid = valueParsableAsTypeCollectionFieldConfigurationValidator.isValid(new CollectionFieldConfigurationDto())

        then:
        valid
    }

    def "should return false if one field parsability validator returns false"() {
        given:
        collectionFieldParsabilityValidator1.isValid(_ as CollectionFieldConfigurationDto) >> true
        collectionFieldParsabilityValidator2.isValid(_ as CollectionFieldConfigurationDto) >> false

        when:
        boolean valid = valueParsableAsTypeCollectionFieldConfigurationValidator.isValid(new CollectionFieldConfigurationDto())

        then:
        !valid
    }
}
