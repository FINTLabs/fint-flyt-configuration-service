package no.novari.flyt.configuration.validation.constraints;

import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto;
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping;
import no.novari.flyt.configuration.model.metadata.InstanceValueMetadata;
import no.novari.flyt.configuration.validation.ConfigurationValidationContext;
import no.novari.flyt.configuration.validation.instancereference.InstanceValueKeyExtractionService;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InstanceValueKeysAreDefinedInMetadataValidatorTest {

    private InstanceValueKeysAreDefinedInMetadataValidator instanceValueExistInMetadataValidator;

    @BeforeEach
    void setup() {
        instanceValueExistInMetadataValidator = new InstanceValueKeysAreDefinedInMetadataValidator(
                new InstanceValueKeyExtractionService()
        );
    }

    @Test
    void shouldReturnTrueWhenValueContainsNoInstanceFieldReferences() {
        ConfigurationValidationContext configurationValidationContext = ConfigurationValidationContext
                .builder()
                .instanceValueTypePerKey(Map.of(
                        "instanceValueKey1", InstanceValueMetadata.Type.STRING,
                        "instanceValueKey2", InstanceValueMetadata.Type.STRING,
                        "instanceValueKey3", InstanceValueMetadata.Type.STRING
                ))
                .build();
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = mock(HibernateConstraintValidatorContext.class);
        when(hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class))
                .thenReturn(configurationValidationContext);

        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.DYNAMIC_STRING)
                .mappingString("Title")
                .build();

        boolean valid = instanceValueExistInMetadataValidator.isValid(
                valueMappingDto,
                hibernateConstraintValidatorContext
        );

        assertTrue(valid);
    }

    @Test
    void shouldReturnTrueWhenAllInstanceFieldReferenceKeysAreFoundInMetadata() {
        ConfigurationValidationContext configurationValidationContext = ConfigurationValidationContext
                .builder()
                .instanceValueTypePerKey(Map.of(
                        "instanceValueKey1", InstanceValueMetadata.Type.STRING,
                        "instanceValueKey2", InstanceValueMetadata.Type.STRING,
                        "instanceValueKey3", InstanceValueMetadata.Type.STRING
                ))
                .build();
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = mock(HibernateConstraintValidatorContext.class);
        when(hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class))
                .thenReturn(configurationValidationContext);

        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.DYNAMIC_STRING)
                .mappingString("Title $if{instanceValueKey1} $if{instanceValueKey2}")
                .build();

        boolean valid = instanceValueExistInMetadataValidator.isValid(
                valueMappingDto,
                hibernateConstraintValidatorContext
        );

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseWhenAnInstanceFieldReferenceKeyIsNotFoundInMetadata() {
        ConfigurationValidationContext configurationValidationContext = ConfigurationValidationContext
                .builder()
                .instanceValueTypePerKey(Map.of(
                        "instanceValueKey1", InstanceValueMetadata.Type.STRING,
                        "instanceValueKey3", InstanceValueMetadata.Type.STRING
                ))
                .build();
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = mock(HibernateConstraintValidatorContext.class);
        when(hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class))
                .thenReturn(configurationValidationContext);

        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.DYNAMIC_STRING)
                .mappingString("Title $if{instanceValueKey1} $if{instanceValueKey2}")
                .build();

        boolean valid = instanceValueExistInMetadataValidator.isValid(
                valueMappingDto,
                hibernateConstraintValidatorContext
        );

        assertFalse(valid);
    }
}
