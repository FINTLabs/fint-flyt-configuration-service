package no.fintlabs.validation.parsability

import no.fintlabs.model.configuration.dtos.FieldConfigurationDto
import no.fintlabs.model.configuration.entities.FieldConfiguration
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
        FieldConfigurationDto fieldConfigurationDto = FieldConfigurationDto
                .builder()
                .type(FieldConfiguration.Type.STRING)
                .value("value")
                .build()

        when:
        boolean valid = fieldParsabilityValidator.isValid(fieldConfigurationDto)

        then:
        1 * fieldParsabilityValidator.isValid("value")
        !valid
    }

    def 'should return true if validator type does not match value type'() {
        given:
        FieldConfigurationDto fieldConfigurationDto = FieldConfigurationDto
                .builder()
                .type(FieldConfiguration.Type.BOOLEAN)
                .value("value")
                .build()

        when:
        boolean valid = fieldParsabilityValidator.isValid(fieldConfigurationDto)

        then:
        0 * fieldParsabilityValidator.isValid("value")
        valid
    }

    def 'should return true if value is null'() {
        given:
        FieldConfigurationDto fieldConfigurationDto = FieldConfigurationDto
                .builder()
                .type(FieldConfiguration.Type.STRING)
                .value(null)
                .build()

        when:
        boolean valid = fieldParsabilityValidator.isValid(fieldConfigurationDto)

        then:
        0 * fieldParsabilityValidator.isValid(_ as String)
        valid
    }

}
