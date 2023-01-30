package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.ValueMappingDto;
import no.fintlabs.validation.ConfigurationValidationContext;
import no.fintlabs.validation.instancefield.InstanceFieldReferenceKeyExtractionService;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static no.fintlabs.validation.constraints.InstanceFieldReferenceKeysExistInMetadata.KEYS_MISSING_IN_METADATA;

@Service
public class InstanceFieldReferenceKeysExistInMetadataValidator
        implements HibernateConstraintValidator<InstanceFieldReferenceKeysExistInMetadata, ValueMappingDto> {

    private final InstanceFieldReferenceKeyExtractionService instanceFieldReferenceKeyExtractionService;

    public InstanceFieldReferenceKeysExistInMetadataValidator(
            InstanceFieldReferenceKeyExtractionService instanceFieldReferenceKeyExtractionService
    ) {
        this.instanceFieldReferenceKeyExtractionService = instanceFieldReferenceKeyExtractionService;
    }

    @Override
    public boolean isValid(ValueMappingDto valueMappingDto, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
        Collection<String> configurationInstanceFieldReferenceKeys = instanceFieldReferenceKeyExtractionService
                .extractIfReferenceKeys(valueMappingDto.getMappingString());

        Set<String> metadataInstanceFieldKeySet = hibernateConstraintValidatorContext
                .getConstraintValidatorPayload(ConfigurationValidationContext.class)
                .getMetadataInstanceFieldTypePerKey()
                .keySet();

        List<String> keysMissingInMetadata = configurationInstanceFieldReferenceKeys
                .stream()
                .filter(key -> !metadataInstanceFieldKeySet.contains(key)).toList();

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
