package no.novari.configuration.validation.valueparsability;

import no.novari.configuration.model.configuration.dtos.ValueMappingDto;
import no.novari.configuration.model.configuration.entities.ValueMapping;
import no.novari.configuration.validation.valueparsability.ValueParsabilityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class ValueParsabilityValidatorTest {

    ValueParsabilityValidator valueParsabilityValidator;

    @BeforeEach
    void setup() {
        valueParsabilityValidator = mock(ValueParsabilityValidator.class);
        when(valueParsabilityValidator.getTypeToValidate()).thenReturn(ValueMapping.Type.STRING);
        when(valueParsabilityValidator.isValid(anyString())).thenReturn(false);
    }

    @Test
    void shouldValidateValueIfValidatorTypeMatchesValueType() {
        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.STRING)
                .mappingString("value")
                .build();

        when(valueParsabilityValidator.isValid(valueMappingDto)).thenCallRealMethod();
        boolean valid = valueParsabilityValidator.isValid(valueMappingDto);

        verify(valueParsabilityValidator, times(1)).isValid("value");
        assertFalse(valid);
    }

    @Test
    void shouldReturnTrueIfValidatorTypeDoesNotMatchValueType() {
        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.BOOLEAN)
                .mappingString("value")
                .build();

        when(valueParsabilityValidator.isValid(valueMappingDto)).thenCallRealMethod();
        boolean valid = valueParsabilityValidator.isValid(valueMappingDto);

        verify(valueParsabilityValidator, times(0)).isValid("value");
        assertTrue(valid);
    }

    @Test
    void shouldReturnTrueIfValueIsNull() {
        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.STRING)
                .mappingString(null)
                .build();

        when(valueParsabilityValidator.isValid(valueMappingDto)).thenCallRealMethod();
        boolean valid = valueParsabilityValidator.isValid(valueMappingDto);

        verify(valueParsabilityValidator, times(0)).isValid(anyString());
        assertTrue(valid);
    }
}
