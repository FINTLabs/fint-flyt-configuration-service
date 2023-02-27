package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.dtos.object.ObjectMappingDto
import no.fintlabs.model.configuration.dtos.value.ValueMappingDto
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import spock.lang.Specification

class AtLeastOneChildValidatorSpec extends Specification {

    AtLeastOneChildValidator atLeastOneChildValidator;

    def setup() {
        atLeastOneChildValidator = new AtLeastOneChildValidator();
    }

    def 'should return true when object mapping has children'() {
        given:
        ObjectMappingDto objectMappingDto = ObjectMappingDto.builder().build()
        objectMappingDto.getValueMappingPerKey().put("valueMappingKey", Mock(ValueMappingDto.class))

        when:
        boolean valid = atLeastOneChildValidator.isValid(
                objectMappingDto,
                Mock(HibernateConstraintValidatorContext.class)
        )

        then:
        valid
    }

    def 'should return false when object mapping has no children'() {
        when:
        boolean valid = atLeastOneChildValidator.isValid(
                ObjectMappingDto.builder().build(),
                Mock(HibernateConstraintValidatorContext.class)
        )

        then:
        !valid
    }

}
