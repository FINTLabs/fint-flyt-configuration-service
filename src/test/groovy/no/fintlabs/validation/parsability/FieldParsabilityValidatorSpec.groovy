package no.fintlabs.validation.parsability

import no.fintlabs.model.configuration.dtos.ValueMappingDto
import no.fintlabs.model.configuration.entities.ValueMapping
import spock.lang.Specification

class FieldParsabilityValidatorSpec extends Specification {

    FieldParsabilityValidator fieldParsabilityValidator

    def setup() {
        fieldParsabilityValidator = Spy(
                new FieldParsabilityValidator() {
                    @Override
                    ValueMapping.Type getTypeToValidate() {
                        return ValueMapping.Type.STRING
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
        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.STRING)
                .mappingString("value")
                .build()

        when:
        boolean valid = fieldParsabilityValidator.isValid(valueMappingDto)

        then:
        1 * fieldParsabilityValidator.isValid("value")
        !valid
    }

    def 'should return true if validator type does not match value type'() {
        given:
        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.BOOLEAN)
                .mappingString("value")
                .build()

        when:
        boolean valid = fieldParsabilityValidator.isValid(valueMappingDto)

        then:
        0 * fieldParsabilityValidator.isValid("value")
        valid
    }

    def 'should return true if value is null'() {
        given:
        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.STRING)
                .mappingString(null)
                .build()

        when:
        boolean valid = fieldParsabilityValidator.isValid(valueMappingDto)

        then:
        0 * fieldParsabilityValidator.isValid(_ as String)
        valid
    }

}
