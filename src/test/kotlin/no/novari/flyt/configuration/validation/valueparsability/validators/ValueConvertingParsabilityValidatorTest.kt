package no.novari.flyt.configuration.validation.valueparsability.validators

import no.novari.flyt.configuration.model.configuration.entities.ValueMapping
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ValueConvertingParsabilityValidatorTest {
    private lateinit var validator: ValueConvertingParsabilityValidator

    @BeforeEach
    fun setup() {
        validator = ValueConvertingParsabilityValidator()
    }

    @Test
    fun getTypeToValidate() {
        assertEquals(ValueMapping.Type.VALUE_CONVERTING, validator.getTypeToValidate())
    }

    @Test
    fun shouldReturnTrueForStringWithValueConvertingAndInstanceFieldReferenceOfCorrectSyntax() {
        assertTrue(validator.isValid("\$vc{0}\$if{fornavn}"))
    }

    @Test
    fun shouldReturnTrueForStringWithValueConvertingAndInstanceCollectionFieldReferenceOfCorrectSyntax() {
        assertTrue(validator.isValid("\$vc{0}\$icf{0}{fornavn}"))
    }

    @Test
    fun shouldReturnFalseForEmptyString() {
        assertFalse(validator.isValid(""))
    }

    @Test
    fun shouldReturnFalseForBlankString() {
        assertFalse(validator.isValid(" "))
    }

    @Test
    fun shouldReturnFalseForStringWithoutDynamicValues() {
        assertFalse(validator.isValid("Søknad VGS"))
    }

    @Test
    fun shouldReturnFalseForStringWithValueConvertingWithWrongSyntax() {
        assertFalse(validator.isValid("asd\$if{fornavn}"))
    }

    @Test
    fun shouldReturnFalseForStringWithValueConvertingWithStringId() {
        assertFalse(validator.isValid("\$vc(asd)\$if{fornavn}"))
    }

    @Test
    fun shouldReturnFalseForStringWithValueConverterReferenceLast() {
        assertFalse(validator.isValid("\$if{fornavn}\$vc(asd)"))
    }

    @Test
    fun shouldReturnFalseForStringWithoutValueConverterReference() {
        assertFalse(validator.isValid("\$if{fornavn}"))
    }

    @Test
    fun shouldReturnFalseForStringWithoutInstanceFieldReference() {
        assertFalse(validator.isValid("\$if{fornavn}"))
    }

    @Test
    fun shouldReturnFalseForStringWithValueConvertingAndInstanceFieldReferenceOfCorrectSyntaxButAdditionalText() {
        assertFalse(validator.isValid("\$vc(0)a\$if{fornavn}"))
    }
}
