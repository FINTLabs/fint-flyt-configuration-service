package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.dtos.ValueMappingDto
import no.fintlabs.model.configuration.entities.ValueMapping
import no.fintlabs.validation.valueparsability.ValueParsabilityValidator
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import spock.lang.Specification

class ValueParsableAsTypeValidatorSpec extends Specification {

    ValueParsabilityValidator valueParsabilityValidator1
    ValueParsabilityValidator valueParsabilityValidator2
    ValueParsableAsTypeValidator valueParsableAsTypeValidator
    HibernateConstraintValidatorContext hibernateConstraintValidatorContext

    def setup() {
        valueParsabilityValidator1 = Mock(ValueParsabilityValidator.class)
        valueParsabilityValidator2 = Mock(ValueParsabilityValidator.class)
        valueParsableAsTypeValidator = new ValueParsableAsTypeValidator(
                List.of(valueParsabilityValidator1, valueParsabilityValidator2)
        )
        hibernateConstraintValidatorContext = Mock(HibernateConstraintValidatorContext.class)
        hibernateConstraintValidatorContext.addMessageParameter(_ as String, _) >> hibernateConstraintValidatorContext

    }

    def "should return true if all field parsability validators return true"() {
        given:
        valueParsabilityValidator1.isValid(_ as ValueMappingDto) >> true
        valueParsabilityValidator2.isValid(_ as ValueMappingDto) >> true

        when:
        boolean valid = valueParsableAsTypeValidator.isValid(
                ValueMappingDto.builder().type(ValueMapping.Type.STRING).build(),
                Mock(HibernateConstraintValidatorContext.class)
        )

        then:
        valid
    }

    def "should return false if one field parsability validator returns false"() {
        given:
        valueParsabilityValidator1.isValid(_ as ValueMappingDto) >> true
        valueParsabilityValidator2.isValid(_ as ValueMappingDto) >> false

        when:
        boolean valid = valueParsableAsTypeValidator.isValid(
                ValueMappingDto.builder().type(ValueMapping.Type.STRING).build(),
                Mock(HibernateConstraintValidatorContext.class)
        )

        then:
        !valid
    }

}
