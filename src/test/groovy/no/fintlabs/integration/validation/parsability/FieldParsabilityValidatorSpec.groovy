package no.fintlabs.integration.validation.parsability

import no.fintlabs.integration.model.configuration.FieldConfiguration
import spock.lang.Specification

class FieldParsabilityValidatorSpec extends Specification {

    FieldParsabilityValidator fieldParsabilityValidator

    def setup() {
        fieldParsabilityValidator = Spy(
                new FieldParsabilityValidator() {
                    @Override
                    FieldConfiguration.Type getTypeToValidate() {
                        return FieldConfiguration.Type.STRING
                    }

                    @Override
                    boolean isValid(String value) {
                        return false
                    }
                }
        )
    }

    def 'should validate value if validator type matches value type'() {
        given:
        FieldConfiguration fieldConfiguration = new FieldConfiguration()
        fieldConfiguration.setType(FieldConfiguration.Type.STRING)
        fieldConfiguration.setValue("value")

        when:
        boolean valid = fieldParsabilityValidator.isValid(fieldConfiguration)

        then:
        1 * fieldParsabilityValidator.isValid("value")
        !valid
    }

    def 'should return true if validator type does not match value type'() {
        given:
        FieldConfiguration fieldConfiguration = new FieldConfiguration()
        fieldConfiguration.setType(FieldConfiguration.Type.BOOLEAN)
        fieldConfiguration.setValue("value")

        when:
        boolean valid = fieldParsabilityValidator.isValid(fieldConfiguration)

        then:
        0 * fieldParsabilityValidator.isValid("value")
        valid
    }

}
