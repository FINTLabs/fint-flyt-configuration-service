package no.fintlabs.integration.validation.constraints

import no.fintlabs.integration.model.FieldCollectionConfiguration
import no.fintlabs.integration.validation.parsability.FieldCollectionParsabilityValidator
import spock.lang.Specification

class ValueParsableAsTypeFieldCollectionConfigurationValidatorSpec extends Specification {

    FieldCollectionParsabilityValidator fieldCollectionParsabilityValidator1
    FieldCollectionParsabilityValidator fieldCollectionParsabilityValidator2
    ValueParsableAsTypeFieldCollectionConfigurationValidator valueParsableAsTypeFieldCollectionConfigurationValidator

    def setup() {
        fieldCollectionParsabilityValidator1 = Mock(FieldCollectionParsabilityValidator.class)
        fieldCollectionParsabilityValidator2 = Mock(FieldCollectionParsabilityValidator.class)
        valueParsableAsTypeFieldCollectionConfigurationValidator = new ValueParsableAsTypeFieldCollectionConfigurationValidator(
                List.of(fieldCollectionParsabilityValidator1, fieldCollectionParsabilityValidator2)
        )
    }

    def "should return true if all field parsability validators return true"() {
        given:
        fieldCollectionParsabilityValidator1.isValid(_ as FieldCollectionConfiguration) >> true
        fieldCollectionParsabilityValidator2.isValid(_ as FieldCollectionConfiguration) >> true

        when:
        boolean valid = valueParsableAsTypeFieldCollectionConfigurationValidator.isValid(new FieldCollectionConfiguration())

        then:
        valid
    }

    def "should return false if one field parsability validator returns false"() {
        given:
        fieldCollectionParsabilityValidator1.isValid(_ as FieldCollectionConfiguration) >> true
        fieldCollectionParsabilityValidator2.isValid(_ as FieldCollectionConfiguration) >> false

        when:
        boolean valid = valueParsableAsTypeFieldCollectionConfigurationValidator.isValid(new FieldCollectionConfiguration())

        then:
        !valid
    }
}
