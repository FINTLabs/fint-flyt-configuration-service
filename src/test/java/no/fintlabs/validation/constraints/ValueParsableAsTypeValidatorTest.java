package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.ValueMappingDto;
import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.validation.valueparsability.ValueParsabilityValidator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ValueParsableAsTypeValidatorTest {

    private ValueParsabilityValidator valueParsabilityValidator1;
    private ValueParsabilityValidator valueParsabilityValidator2;
    private ValueParsableAsTypeValidator valueParsableAsTypeValidator;

    @BeforeEach
    void setup() {
        valueParsabilityValidator1 = mock(ValueParsabilityValidator.class);
        valueParsabilityValidator2 = mock(ValueParsabilityValidator.class);
        valueParsableAsTypeValidator = new ValueParsableAsTypeValidator(
                Arrays.asList(valueParsabilityValidator1, valueParsabilityValidator2)
        );
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = mock(HibernateConstraintValidatorContext.class);
        when(hibernateConstraintValidatorContext.addMessageParameter(any(String.class), any())).thenReturn(hibernateConstraintValidatorContext);
    }

    @Test
    void shouldReturnTrueIfAllFieldParsabilityValidatorsReturnTrue() {
        when(valueParsabilityValidator1.isValid(any(ValueMappingDto.class))).thenReturn(true);
        when(valueParsabilityValidator2.isValid(any(ValueMappingDto.class))).thenReturn(true);

        boolean valid = valueParsableAsTypeValidator.isValid(
                ValueMappingDto.builder().type(ValueMapping.Type.STRING).build(),
                mock(HibernateConstraintValidatorContext.class)
        );

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseIfOneFieldParsabilityValidatorReturnsFalse() {
        when(valueParsabilityValidator1.isValid(any(ValueMappingDto.class))).thenReturn(true);
        when(valueParsabilityValidator2.isValid(any(ValueMappingDto.class))).thenReturn(false);

        boolean valid = valueParsableAsTypeValidator.isValid(
                ValueMappingDto.builder().type(ValueMapping.Type.STRING).build(),
                mock(HibernateConstraintValidatorContext.class)
        );

        assertFalse(valid);
    }
}
