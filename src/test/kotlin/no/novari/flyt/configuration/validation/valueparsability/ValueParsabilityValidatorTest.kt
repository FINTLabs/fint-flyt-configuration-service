package no.novari.flyt.configuration.validation.valueparsability

import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ValueParsabilityValidatorTest {
    private lateinit var valueParsabilityValidator: ValueParsabilityValidator

    @BeforeEach
    fun setup() {
        valueParsabilityValidator = mock()
        whenever(valueParsabilityValidator.getTypeToValidate()).thenReturn(ValueMapping.Type.STRING)
        whenever(valueParsabilityValidator.isValid(any<String>())).thenReturn(false)
    }

    @Test
    fun shouldValidateValueIfValidatorTypeMatchesValueType() {
        val valueMappingDto =
            ValueMappingDto
                .builder()
                .type(ValueMapping.Type.STRING)
                .mappingString("value")
                .build()

        whenever(valueParsabilityValidator.isValid(valueMappingDto)).thenCallRealMethod()
        val valid = valueParsabilityValidator.isValid(valueMappingDto)

        verify(valueParsabilityValidator, times(1)).isValid("value")
        assertFalse(valid)
    }

    @Test
    fun shouldReturnTrueIfValidatorTypeDoesNotMatchValueType() {
        val valueMappingDto =
            ValueMappingDto
                .builder()
                .type(ValueMapping.Type.BOOLEAN)
                .mappingString("value")
                .build()

        whenever(valueParsabilityValidator.isValid(valueMappingDto)).thenCallRealMethod()
        val valid = valueParsabilityValidator.isValid(valueMappingDto)

        verify(valueParsabilityValidator, times(0)).isValid("value")
        assertTrue(valid)
    }

    @Test
    fun shouldReturnTrueIfValueIsNull() {
        val valueMappingDto =
            ValueMappingDto
                .builder()
                .type(ValueMapping.Type.STRING)
                .mappingString(null)
                .build()

        whenever(valueParsabilityValidator.isValid(valueMappingDto)).thenCallRealMethod()
        val valid = valueParsabilityValidator.isValid(valueMappingDto)

        verify(valueParsabilityValidator, times(0)).isValid(any<String>())
        assertTrue(valid)
    }
}
