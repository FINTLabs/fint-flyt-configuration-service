package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.ValueMappingDto;
import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.model.metadata.InstanceValueMetadata;
import no.fintlabs.validation.ConfigurationValidationContext;
import no.fintlabs.validation.instancereference.InstanceValueKeyExtractionService;
import no.fintlabs.validation.instancereference.InstanceValueTypeCompatibilityValidator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.util.function.Tuples;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class InstanceValueTypesAreCompatibleValidatorTest {

    @Mock
    private InstanceValueKeyExtractionService instanceValueKeyExtractionService;

    @Mock
    private List<InstanceValueTypeCompatibilityValidator> instanceValueTypeCompatibilityValidators;

    @Mock
    private HibernateConstraintValidatorContext hibernateConstraintValidatorContext;

    private InstanceValueTypesAreCompatibleValidator validator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        instanceValueTypeCompatibilityValidators = new ArrayList<>();
        InstanceValueTypeCompatibilityValidator mockValidator = mock(InstanceValueTypeCompatibilityValidator.class);
        instanceValueTypeCompatibilityValidators.add(mockValidator);

        validator = new InstanceValueTypesAreCompatibleValidator(instanceValueKeyExtractionService, instanceValueTypeCompatibilityValidators);

    }

    @Test
    public void testIsValid_true() {
        ValueMappingDto dto = ValueMappingDto.builder().type(ValueMapping.Type.STRING).mappingString("mapping").build();
        Map<String, InstanceValueMetadata.Type> map = new HashMap<>();
        map.put("key", InstanceValueMetadata.Type.STRING);
        ConfigurationValidationContext configurationValidationContext = ConfigurationValidationContext.builder().instanceValueTypePerKey(map).build();

        when(instanceValueKeyExtractionService.extractIfReferenceKeys(any())).thenReturn(Collections.singletonList("key"));
        when(hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class)).thenReturn(configurationValidationContext);

        for(InstanceValueTypeCompatibilityValidator v : instanceValueTypeCompatibilityValidators) {
            when(v.findIncompatibleInstanceValuesKeyAndType(dto, Collections.singletonList("key"), map)).thenReturn(List.of());
        }
        boolean result = validator.isValid(dto, hibernateConstraintValidatorContext);
        assertTrue(result);
    }

    @Test
    public void testIsValid_false() {
        ValueMappingDto dto = ValueMappingDto.builder().type(ValueMapping.Type.STRING).mappingString("mapping").build();
        Map<String, InstanceValueMetadata.Type> map = new HashMap<>();
        map.put("key", InstanceValueMetadata.Type.FILE);
        ConfigurationValidationContext configurationValidationContext = ConfigurationValidationContext.builder().instanceValueTypePerKey(map).build();

        when(instanceValueKeyExtractionService.extractIfReferenceKeys(any())).thenReturn(Collections.singletonList("key"));
        when(hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class)).thenReturn(configurationValidationContext);

        for(InstanceValueTypeCompatibilityValidator v : instanceValueTypeCompatibilityValidators) {
            when(v.findIncompatibleInstanceValuesKeyAndType(dto, Collections.singletonList("key"), map)).thenReturn(Collections.singletonList(Tuples.of("key", InstanceValueMetadata.Type.FILE)));
        }
        boolean result = validator.isValid(dto, hibernateConstraintValidatorContext);
        assertFalse(result);
    }
}
