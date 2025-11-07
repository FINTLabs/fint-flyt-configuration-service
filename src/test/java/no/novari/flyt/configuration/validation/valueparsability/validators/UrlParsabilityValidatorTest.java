package no.novari.flyt.configuration.validation.valueparsability.validators;

import no.novari.flyt.configuration.model.configuration.entities.ValueMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlParsabilityValidatorTest {

    UrlParsabilityValidator urlParsabilityValidator;

    @BeforeEach
    void setup() {
        urlParsabilityValidator = new UrlParsabilityValidator();
    }

    @Test
    void getTypeToValidate() {
        assertEquals(ValueMapping.Type.URL, urlParsabilityValidator.getTypeToValidate());
    }

    @Test
    void shouldReturnTrueIfValueIsParsableAsURL() {
        boolean valid = urlParsabilityValidator.isValid("http://www.example.com");

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseIfValueIsNotParsableAsURL() {
        boolean valid = urlParsabilityValidator.isValid("httpkk://www.example.com");

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseIfValueIsEmpty() {
        boolean valid = urlParsabilityValidator.isValid("");

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseIfValueIsBlank() {
        boolean valid = urlParsabilityValidator.isValid(" ");

        assertFalse(valid);
    }

}
