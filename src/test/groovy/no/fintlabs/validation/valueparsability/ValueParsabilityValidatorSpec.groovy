package no.fintlabs.validation.valueparsability

import no.fintlabs.model.configuration.dtos.value.ValueMappingDto
import no.fintlabs.model.configuration.entities.value.ValueMapping
import spock.lang.Specification

class ValueParsabilityValidatorSpec extends Specification {

    ValueParsabilityValidator valueParsabilityValidator

    def setup() {
        valueParsabilityValidator = Spy(
                new ValueParsabilityValidator() {
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
        boolean valid = valueParsabilityValidator.isValid(valueMappingDto)

        then:
        1 * valueParsabilityValidator.isValid("value")
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
        boolean valid = valueParsabilityValidator.isValid(valueMappingDto)

        then:
        0 * valueParsabilityValidator.isValid("value")
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
        boolean valid = valueParsabilityValidator.isValid(valueMappingDto)

        then:
        0 * valueParsabilityValidator.isValid(_ as String)
        valid
    }

}
