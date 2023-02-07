package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.dtos.ElementMappingDto
import no.fintlabs.model.configuration.dtos.ValueMappingDto
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import spock.lang.Specification

class AtLeastOneChildValidatorSpec extends Specification {

    AtLeastOneChildValidator atLeastOneChildValidator;

    def setup() {
        atLeastOneChildValidator = new AtLeastOneChildValidator();
    }

    def 'should return true when element mapping has children'() {
        given:
        ElementMappingDto elementMappingDto = new ElementMappingDto()
        elementMappingDto.getValueMappingPerKey().put("valueMappingKey", Mock(ValueMappingDto.class))

        when:
        boolean valid = atLeastOneChildValidator.isValid(
                elementMappingDto,
                Mock(HibernateConstraintValidatorContext.class)
        )

        then:
        valid
    }

    def 'should return false when element mapping has no children'() {
        when:
        boolean valid = atLeastOneChildValidator.isValid(
                new ElementMappingDto(),
                Mock(HibernateConstraintValidatorContext.class)
        )

        then:
        !valid
    }

}
