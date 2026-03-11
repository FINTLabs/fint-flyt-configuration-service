package no.novari.flyt.configuration.validation.valueparsability.validators

import no.novari.flyt.configuration.model.configuration.entities.ValueMapping
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BooleanParsabilityValidatorTest {
    private lateinit var booleanParsabilityValidator: BooleanParsabilityValidator

    @BeforeEach
    fun setup() {
        booleanParsabilityValidator = BooleanParsabilityValidator()
    }

    @Test
    fun getTypeToValidate() {
        assertEquals(ValueMapping.Type.BOOLEAN, booleanParsabilityValidator.getTypeToValidate())
    }

    @Test
    fun shouldReturnTrueIfValueIsTrue() {
        assertTrue(booleanParsabilityValidator.isValid("true"))
    }

    @Test
    fun shouldReturnTrueIfValueIsFalse() {
        assertTrue(booleanParsabilityValidator.isValid("false"))
    }

    @Test
    fun shouldReturnFalseIfValueIsEmpty() {
        assertFalse(booleanParsabilityValidator.isValid(""))
    }

    @Test
    fun shouldReturnFalseIfValueIsBlank() {
        assertFalse(booleanParsabilityValidator.isValid(" "))
    }

    @Test
    fun shouldReturnFalseIfValueIsTruest() {
        assertFalse(booleanParsabilityValidator.isValid("truest"))
    }
}
