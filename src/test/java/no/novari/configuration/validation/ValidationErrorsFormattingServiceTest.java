package no.novari.configuration.validation;

import no.novari.configuration.validation.ValidationErrorsFormattingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidationErrorsFormattingServiceTest {

    private ValidationErrorsFormattingService service;

    @BeforeEach
    void setUp() {
        service = new ValidationErrorsFormattingService();
    }

    @Test
    void formatNoErrors() {
        Set<ConstraintViolation<Object>> errors = new HashSet<>();
        String result = service.format(errors);
        assertEquals("Validation error: []", result);
    }

    @Test
    void formatSingleError() {
        Set<ConstraintViolation<Object>> errors = new HashSet<>();

        ConstraintViolation<Object> mockViolation = mock(ConstraintViolation.class);
        when(mockViolation.getMessage()).thenReturn("Invalid value");
        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("field");
        when(mockViolation.getPropertyPath()).thenReturn(mockPath);

        errors.add(mockViolation);

        String result = service.format(errors);
        assertEquals("Validation error: ['field Invalid value']", result);
    }

    @Test
    void formatMultipleErrors() {
        Set<ConstraintViolation<Object>> errors = new HashSet<>();

        ConstraintViolation<Object> mockViolation1 = mock(ConstraintViolation.class);
        when(mockViolation1.getMessage()).thenReturn("Invalid value");
        Path mockPath1 = mock(Path.class);
        when(mockPath1.toString()).thenReturn("field");
        when(mockViolation1.getPropertyPath()).thenReturn(mockPath1);

        ConstraintViolation<Object> mockViolation2 = mock(ConstraintViolation.class);
        when(mockViolation2.getMessage()).thenReturn("Invalid format");
        Path mockPath2 = mock(Path.class);
        when(mockPath2.toString()).thenReturn("otherField");
        when(mockViolation2.getPropertyPath()).thenReturn(mockPath2);

        errors.add(mockViolation1);
        errors.add(mockViolation2);

        String result = service.format(errors);
        assertEquals("Validation errors: ['field Invalid value', 'otherField Invalid format']", result);
    }

    @Test
    void formatBlankPropertyPath() {
        Set<ConstraintViolation<Object>> errors = new HashSet<>();

        ConstraintViolation<Object> mockViolation = mock(ConstraintViolation.class);
        when(mockViolation.getMessage()).thenReturn("Invalid value");
        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("");
        when(mockViolation.getPropertyPath()).thenReturn(mockPath);

        errors.add(mockViolation);

        String result = service.format(errors);
        assertEquals("Validation error: ['Invalid value']", result);
    }
}
