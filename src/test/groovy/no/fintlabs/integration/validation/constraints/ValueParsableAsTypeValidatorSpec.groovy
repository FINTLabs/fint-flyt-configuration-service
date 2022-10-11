package no.fintlabs.integration.validation.constraints

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import spock.lang.Specification

import static no.fintlabs.integration.validation.constraints.ValueParsableAsType.FIELD_VALUE_TYPE_REF

class ValueParsableAsTypeValidatorSpec extends Specification {

    ValueParsableAsTypeValidator valueParsableAsTypeValidator
    HibernateConstraintValidatorContext constraintValidatorContext

    def setup() {
        valueParsableAsTypeValidator = Spy(
                new ValueParsableAsTypeValidator() {

                    @Override
                    String getType(Object value) {
                        return "STRING"
                    }

                    @Override
                    boolean isValid(Object value) {
                        return false
                    }
                }
        )
        constraintValidatorContext = Mock(HibernateConstraintValidatorContext.class)
        constraintValidatorContext.unwrap(_ as Class) >> constraintValidatorContext
    }

    def 'should return true if abstract validation returns true'() {
        given:
        Object object = new Object()

        when:
        boolean valid = valueParsableAsTypeValidator.isValid(object, constraintValidatorContext)

        then:
        valid
        1 * valueParsableAsTypeValidator.isValid(object, constraintValidatorContext)
        1 * valueParsableAsTypeValidator.isValid(object) >> true
        0 * _
    }

    def 'should return false and add error message parameters if abstract validation returns false'() {
        given:
        Object object = new Object()

        when:
        boolean valid = valueParsableAsTypeValidator.isValid(object, constraintValidatorContext)

        then:
        !valid
        1 * valueParsableAsTypeValidator.isValid(object, constraintValidatorContext)
        1 * valueParsableAsTypeValidator.isValid(object) >> false
        1 * valueParsableAsTypeValidator.getType(object)
        1 * constraintValidatorContext.unwrap(_ as Class) >> constraintValidatorContext
        1 * constraintValidatorContext.addMessageParameter(FIELD_VALUE_TYPE_REF, "STRING") >> constraintValidatorContext
        0 * _
    }

}
