package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.value.ValueMappingDto;
import no.fintlabs.validation.ConfigurationValidationContext;
import no.fintlabs.validation.instancereference.InstanceValueKeyExtractionService;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static no.fintlabs.validation.constraints.InstanceValueKeysAreDefinedInMetadata.KEYS_MISSING_IN_METADATA;

@Service
public class InstanceValueKeysAreDefinedInMetadataValidator
        implements HibernateConstraintValidator<InstanceValueKeysAreDefinedInMetadata, ValueMappingDto> {

    private final InstanceValueKeyExtractionService instanceValueKeyExtractionService;

    public InstanceValueKeysAreDefinedInMetadataValidator(
            InstanceValueKeyExtractionService instanceValueKeyExtractionService
    ) {
        this.instanceValueKeyExtractionService = instanceValueKeyExtractionService;
    }

    @Override
    public boolean isValid(ValueMappingDto valueMappingDto, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
        Collection<String> referencedInstanceValueKeys = instanceValueKeyExtractionService
                .extractIfReferenceKeys(valueMappingDto.getMappingString());

        Set<String> metadataInstanceValueKeySet = hibernateConstraintValidatorContext
                .getConstraintValidatorPayload(ConfigurationValidationContext.class)
                .getInstanceValueTypePerKey()
                .keySet();

        List<String> keysMissingInMetadata = referencedInstanceValueKeys
                .stream()
                .filter(key -> !metadataInstanceValueKeySet.contains(key)).toList();

        if (keysMissingInMetadata.isEmpty()) {
            return true;
        }
        hibernateConstraintValidatorContext.addMessageParameter(
                KEYS_MISSING_IN_METADATA,
                keysMissingInMetadata
        );
        return false;
    }

}
