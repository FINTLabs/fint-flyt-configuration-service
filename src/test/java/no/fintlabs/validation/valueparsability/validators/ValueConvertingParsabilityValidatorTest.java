package no.fintlabs.validation.valueparsability.validators;

import no.fintlabs.model.configuration.entities.ValueMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueConvertingParsabilityValidatorTest {

    ValueConvertingParsabilityValidator valueConvertingParsabilityValidator;

    @BeforeEach
    void setup() {
        valueConvertingParsabilityValidator = new ValueConvertingParsabilityValidator();
    }

    @Test
    void getTypeToValidate() {
        assertEquals(ValueMapping.Type.VALUE_CONVERTING, valueConvertingParsabilityValidator.getTypeToValidate());
    }

    @Test
    void shouldReturnTrueForStringWithValueConvertingAndInstanceFieldReferenceOfCorrectSyntax() {
        boolean valid = valueConvertingParsabilityValidator.isValid("$vc{0}$if{fornavn}");

        assertTrue(valid);
    }

    @Test
    void shouldReturnTrueForStringWithValueConvertingAndInstanceCollectionFieldReferenceOfCorrectSyntax() {
        boolean valid = valueConvertingParsabilityValidator.isValid("$vc{0}$icf{0}{fornavn}");

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseForEmptyString() {
        boolean valid = valueConvertingParsabilityValidator.isValid("");

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForBlankString() {
        boolean valid = valueConvertingParsabilityValidator.isValid(" ");

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForStringWithoutDynamicValues() {
        boolean valid = valueConvertingParsabilityValidator.isValid("Søknad VGS");

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForStringWithValueConvertingWithWrongSyntax() {
        boolean valid = valueConvertingParsabilityValidator.isValid("asd$if{fornavn}");

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForStringWithValueConvertingWithStringId() {
        boolean valid = valueConvertingParsabilityValidator.isValid("$vc(asd)$if{fornavn}");

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForStringWithValueConverterReferenceLast() {
        boolean valid = valueConvertingParsabilityValidator.isValid("$if{fornavn}$vc(asd)");

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForStringWithoutValueConverterReference() {
        boolean valid = valueConvertingParsabilityValidator.isValid("$if{fornavn}");

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForStringWithoutInstanceFieldReference() {
        boolean valid = valueConvertingParsabilityValidator.isValid("$if{fornavn}");

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForStringWithValueConvertingAndInstanceFieldReferenceOfCorrectSyntaxButAdditionalText() {
        boolean valid = valueConvertingParsabilityValidator.isValid("$vc(0)a$if{fornavn}");

        assertFalse(valid);
    }
}
