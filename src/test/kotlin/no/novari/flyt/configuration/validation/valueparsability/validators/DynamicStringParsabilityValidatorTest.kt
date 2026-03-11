package no.novari.flyt.configuration.validation.valueparsability.validators

import no.novari.flyt.configuration.model.configuration.entities.ValueMapping
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DynamicStringParsabilityValidatorTest {
    private lateinit var dynamicStringParsabilityValidator: DynamicStringParsabilityValidator

    @BeforeEach
    fun setup() {
        dynamicStringParsabilityValidator = DynamicStringParsabilityValidator()
    }

    @Test
    fun getTypeToValidate() {
        assertEquals(ValueMapping.Type.DYNAMIC_STRING, dynamicStringParsabilityValidator.getTypeToValidate())
    }

    @Test
    fun shouldReturnTrueForEmptyString() {
        assertTrue(dynamicStringParsabilityValidator.isValid(""))
    }

    @Test
    fun shouldReturnTrueForBlankString() {
        assertTrue(dynamicStringParsabilityValidator.isValid(" "))
    }

    @Test
    fun shouldReturnTrueForStringWithoutDynamicValues() {
        assertTrue(dynamicStringParsabilityValidator.isValid("Søknad VGS"))
    }

    @Test
    fun shouldReturnTrueForStringWithDynamicValuesOfCorrectSyntax() {
        assertTrue(
            dynamicStringParsabilityValidator.isValid(
                "Søknad VGS \$if{fornavn}\$if{etter-navn} \$if{person nr1 fødselsdato} for dato \$if{dato} ettellerannet",
            ),
        )
    }

    @Test
    fun shouldReturnTrueForStringContainingSpecialCharactersThatAreNotPartOfAInstanceFieldReferenceCharacters() {
        assertTrue(dynamicStringParsabilityValidator.isValid("Søknad VGS \\$ { } \$if"))
    }

    @Test
    fun shouldReturnFalseForStringContainingIncompleteInstanceFieldReference() {
        assertFalse(dynamicStringParsabilityValidator.isValid("Søknad VGS \$if{fornavn"))
    }

    @Test
    fun shouldReturnFalseForStringContainingEmptyInstanceFieldReference() {
        assertFalse(dynamicStringParsabilityValidator.isValid("Søknad VGS \$if{}"))
    }

    @Test
    fun shouldReturnFalseForStringContainingInstanceFieldReferenceInsideInstanceFieldReference() {
        assertFalse(dynamicStringParsabilityValidator.isValid("Søknad VGS \$if{abc\$if{123}}"))
    }

    @Test
    fun shouldReturnFalseForStringContainingInstanceFieldReferenceStarterInsideInstanceFieldReference() {
        assertFalse(dynamicStringParsabilityValidator.isValid("Søknad VGS \$if{abc\$if{}"))
    }
}
