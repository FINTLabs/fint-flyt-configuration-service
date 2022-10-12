package no.fintlabs.integration.validation.constraints

import no.fintlabs.integration.model.configuration.FieldConfiguration
import no.fintlabs.integration.validation.parsability.FieldParsabilityValidator
import spock.lang.Specification

class ValueParsableAsTypeFieldConfigurationValidatorSpec extends Specification {

    FieldParsabilityValidator fieldParsabilityValidator1
    FieldParsabilityValidator fieldParsabilityValidator2
    ValueParsableAsTypeFieldConfigurationValidator valueParsableAsTypeFieldConfigurationValidator

    def setup() {
        fieldParsabilityValidator1 = Mock(FieldParsabilityValidator.class)
        fieldParsabilityValidator2 = Mock(FieldParsabilityValidator.class)
        valueParsableAsTypeFieldConfigurationValidator = new ValueParsableAsTypeFieldConfigurationValidator(
                List.of(fieldParsabilityValidator1, fieldParsabilityValidator2)
        )
    }

    def "should return true if all field parsability validators return true"() {
        given:
        fieldParsabilityValidator1.isValid(_ as FieldConfiguration) >> true
        fieldParsabilityValidator2.isValid(_ as FieldConfiguration) >> true

        when:
        boolean valid = valueParsableAsTypeFieldConfigurationValidator.isValid(new FieldConfiguration())

        then:
        valid
    }

    def "should return false if one field parsability validator returns false"() {
        given:
        fieldParsabilityValidator1.isValid(_ as FieldConfiguration) >> true
        fieldParsabilityValidator2.isValid(_ as FieldConfiguration) >> false

        when:
        boolean valid = valueParsableAsTypeFieldConfigurationValidator.isValid(new FieldConfiguration())

        then:
        !valid
    }

}
