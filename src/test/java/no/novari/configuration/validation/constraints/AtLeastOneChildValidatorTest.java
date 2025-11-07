package no.novari.configuration.validation.constraints;

import no.novari.configuration.model.configuration.dtos.ObjectMappingDto;
import no.novari.configuration.model.configuration.dtos.ValueMappingDto;
import no.novari.configuration.validation.constraints.AtLeastOneChildValidator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;

class AtLeastOneChildValidatorTest {

    private AtLeastOneChildValidator atLeastOneChildValidator;

    @BeforeEach
    void setup() {
        atLeastOneChildValidator = new AtLeastOneChildValidator();
    }

    @Test
    void shouldReturnTrueWhenObjectMappingHasChildren() {
        ObjectMappingDto objectMappingDto = ObjectMappingDto.builder().build();
        objectMappingDto.getValueMappingPerKey().put("valueMappingKey", mock(ValueMappingDto.class));

        boolean valid = atLeastOneChildValidator.isValid(
                objectMappingDto,
                mock(HibernateConstraintValidatorContext.class)
        );

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseWhenObjectMappingHasNoChildren() {
        boolean valid = atLeastOneChildValidator.isValid(
                ObjectMappingDto.builder().build(),
                mock(HibernateConstraintValidatorContext.class)
        );

        assertFalse(valid);
    }
}
