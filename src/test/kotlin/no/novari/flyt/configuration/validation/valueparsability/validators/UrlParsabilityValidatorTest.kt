package no.novari.flyt.configuration.validation.valueparsability.validators

import no.novari.flyt.configuration.model.configuration.entities.ValueMapping
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UrlParsabilityValidatorTest {
    private lateinit var urlParsabilityValidator: UrlParsabilityValidator

    @BeforeEach
    fun setup() {
        urlParsabilityValidator = UrlParsabilityValidator()
    }

    @Test
    fun getTypeToValidate() {
        assertEquals(ValueMapping.Type.URL, urlParsabilityValidator.getTypeToValidate())
    }

    @Test
    fun shouldReturnTrueIfValueIsParsableAsURL() {
        assertTrue(urlParsabilityValidator.isValid("http://www.example.com"))
    }

    @Test
    fun shouldReturnFalseIfValueIsNotParsableAsURL() {
        assertFalse(urlParsabilityValidator.isValid("httpkk://www.example.com"))
    }

    @Test
    fun shouldReturnFalseIfValueIsEmpty() {
        assertFalse(urlParsabilityValidator.isValid(""))
    }

    @Test
    fun shouldReturnFalseIfValueIsBlank() {
        assertFalse(urlParsabilityValidator.isValid(" "))
    }
}
