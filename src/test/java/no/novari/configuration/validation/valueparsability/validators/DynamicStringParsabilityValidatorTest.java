package no.novari.configuration.validation.valueparsability.validators;

import no.novari.configuration.model.configuration.entities.ValueMapping;
import no.novari.configuration.validation.valueparsability.validators.DynamicStringParsabilityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DynamicStringParsabilityValidatorTest {

    DynamicStringParsabilityValidator dynamicStringParsabilityValidator;

    @BeforeEach
    void setup() {
        dynamicStringParsabilityValidator = new DynamicStringParsabilityValidator();
    }

    @Test
    void getTypeToValidate() {
        assertEquals(ValueMapping.Type.DYNAMIC_STRING, dynamicStringParsabilityValidator.getTypeToValidate());
    }

    @Test
    void shouldReturnTrueForEmptyString() {
        boolean valid = dynamicStringParsabilityValidator.isValid("");
        assertTrue(valid);
    }

    @Test
    void shouldReturnTrueForBlankString() {
        boolean valid = dynamicStringParsabilityValidator.isValid(" ");
        assertTrue(valid);
    }

    @Test
    void shouldReturnTrueForStringWithoutDynamicValues() {
        boolean valid = dynamicStringParsabilityValidator.isValid("Søknad VGS");
        assertTrue(valid);
    }

    @Test
    void shouldReturnTrueForStringWithDynamicValuesOfCorrectSyntax() {
        boolean valid = dynamicStringParsabilityValidator.isValid(
                "Søknad VGS $if{fornavn}$if{etter-navn} $if{person nr1 fødselsdato} for dato $if{dato} ettellerannet");
        assertTrue(valid);
    }

    @Test
    void shouldReturnTrueForStringContainingSpecialCharactersThatAreNotPartOfAInstanceFieldReferenceCharacters() {
        boolean valid = dynamicStringParsabilityValidator.isValid("Søknad VGS \\$ { } $if");
        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseForStringContainingIncompleteInstanceFieldReference() {
        boolean valid = dynamicStringParsabilityValidator.isValid("Søknad VGS $if{fornavn");
        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForStringContainingEmptyInstanceFieldReference() {
        boolean valid = dynamicStringParsabilityValidator.isValid("Søknad VGS $if{}");
        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForStringContainingInstanceFieldReferenceInsideInstanceFieldReference() {
        boolean valid = dynamicStringParsabilityValidator.isValid("Søknad VGS $if{abc$if{123}}");
        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseForStringContainingInstanceFieldReferenceStarterInsideInstanceFieldReference() {
        boolean valid = dynamicStringParsabilityValidator.isValid("Søknad VGS $if{abc$if{}");
        assertFalse(valid);
    }

}
