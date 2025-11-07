package no.novari.configuration.validation.valueparsability.validators;

import no.novari.configuration.model.configuration.entities.ValueMapping;
import no.novari.configuration.validation.valueparsability.validators.BooleanParsabilityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BooleanParsabilityValidatorTest {

    private BooleanParsabilityValidator booleanParsabilityValidator;


    @BeforeEach
    void setup() {
        booleanParsabilityValidator = new BooleanParsabilityValidator();
    }

    @Test
    void getTypeToValidate() {
        assertEquals(ValueMapping.Type.BOOLEAN, booleanParsabilityValidator.getTypeToValidate());
    }

    @Test
    void shouldReturnTrueIfValueIsTrue() {
        boolean valid = booleanParsabilityValidator.isValid("true");
        assertTrue(valid);
    }

    @Test
    void shouldReturnTrueIfValueIsFalse() {
        boolean valid = booleanParsabilityValidator.isValid("false");
        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseIfValueIsEmpty() {
        boolean valid = booleanParsabilityValidator.isValid("");
        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseIfValueIsBlank() {
        boolean valid = booleanParsabilityValidator.isValid(" ");
        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseIfValueIsTruest() {
        boolean valid = booleanParsabilityValidator.isValid("truest");
        assertFalse(valid);
    }
}
