package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.dtos.ValueMappingDto
import no.fintlabs.model.configuration.entities.ValueMapping
import no.fintlabs.validation.parsability.FieldParsabilityValidator
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import spock.lang.Specification

class ValueParsableAsTypeValidatorSpec extends Specification {

    FieldParsabilityValidator fieldParsabilityValidator1
    FieldParsabilityValidator fieldParsabilityValidator2
    ValueParsableAsTypeValidator valueParsableAsTypeValidator
    HibernateConstraintValidatorContext hibernateConstraintValidatorContext

    def setup() {
        fieldParsabilityValidator1 = Mock(FieldParsabilityValidator.class)
        fieldParsabilityValidator2 = Mock(FieldParsabilityValidator.class)
        valueParsableAsTypeValidator = new ValueParsableAsTypeValidator(
                List.of(fieldParsabilityValidator1, fieldParsabilityValidator2)
        )
        hibernateConstraintValidatorContext = Mock(HibernateConstraintValidatorContext.class)
        hibernateConstraintValidatorContext.addMessageParameter(_ as String, _) >> hibernateConstraintValidatorContext

    }

    def "should return true if all field parsability validators return true"() {
        given:
        fieldParsabilityValidator1.isValid(_ as ValueMappingDto) >> true
        fieldParsabilityValidator2.isValid(_ as ValueMappingDto) >> true

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
        fieldParsabilityValidator1.isValid(_ as ValueMappingDto) >> true
        fieldParsabilityValidator2.isValid(_ as ValueMappingDto) >> false

        when:
        boolean valid = valueParsableAsTypeValidator.isValid(
                ValueMappingDto.builder().type(ValueMapping.Type.STRING).build(),
                Mock(HibernateConstraintValidatorContext.class)
        )

        then:
        !valid
    }

}
