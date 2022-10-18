package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.FieldConfigurationDto;
import no.fintlabs.model.metadata.InstanceElementMetadata;
import no.fintlabs.validation.ConfigurationValidationContext;
import no.fintlabs.validation.instancefield.InstanceFieldReferenceKeyExtractionService;
import no.fintlabs.validation.instancefield.InstanceFieldReferenceTypeCompatibilityValidator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static no.fintlabs.validation.constraints.InstanceFieldReferenceValueTypesAreCompatible.CONFIGURATION_FIELD_TYPE;
import static no.fintlabs.validation.constraints.InstanceFieldReferenceValueTypesAreCompatible.INCOMPATIBLE_INSTANCE_FIELDS_KEY_AND_TYPE;

@Service
public class InstanceFieldReferenceValueTypesAreCompatibleValidator implements
        HibernateConstraintValidator<InstanceFieldReferenceValueTypesAreCompatible, FieldConfigurationDto> {

    private final InstanceFieldReferenceKeyExtractionService instanceFieldReferenceKeyExtractionService;
    private final Collection<InstanceFieldReferenceTypeCompatibilityValidator> instanceFieldReferenceTypeCompatibilityValidators;

    public InstanceFieldReferenceValueTypesAreCompatibleValidator(
            InstanceFieldReferenceKeyExtractionService instanceFieldReferenceKeyExtractionService,
            Collection<InstanceFieldReferenceTypeCompatibilityValidator> instanceFieldReferenceTypeCompatibilityValidators
    ) {
        this.instanceFieldReferenceKeyExtractionService = instanceFieldReferenceKeyExtractionService;
        this.instanceFieldReferenceTypeCompatibilityValidators = instanceFieldReferenceTypeCompatibilityValidators;
    }

    @Override
    public boolean isValid(FieldConfigurationDto fieldConfigurationDto, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
        Collection<String> configurationInstanceFieldReferenceKeys = instanceFieldReferenceKeyExtractionService
                .extractIfReferenceKeys(fieldConfigurationDto.getValue());

        Map<String, InstanceElementMetadata.Type> metadataInstanceFieldTypePerKey = hibernateConstraintValidatorContext
                .getConstraintValidatorPayload(ConfigurationValidationContext.class)
                .getMetadataInstanceFieldTypePerKey();

        List<Tuple2<String, InstanceElementMetadata.Type>> incompatibleInstanceFieldsKeyAndType = instanceFieldReferenceTypeCompatibilityValidators
                .stream()
                .map(instanceFieldReferenceTypeCompatibilityValidator -> instanceFieldReferenceTypeCompatibilityValidator
                        .findIncompatibleInstanceFieldsKeyAndType(
                                fieldConfigurationDto,
                                configurationInstanceFieldReferenceKeys,
                                metadataInstanceFieldTypePerKey
                        )
                ).flatMap(Collection::stream)
                .toList();

        if (incompatibleInstanceFieldsKeyAndType.isEmpty()) {
            return true;
        }

        hibernateConstraintValidatorContext.addMessageParameter(CONFIGURATION_FIELD_TYPE, fieldConfigurationDto.getType().name());
        hibernateConstraintValidatorContext.addMessageParameter(
                INCOMPATIBLE_INSTANCE_FIELDS_KEY_AND_TYPE,
                incompatibleInstanceFieldsKeyAndType.stream()
                        .map(tuple -> "{key=" + tuple.getT1() + ", type=" + tuple.getT2() + "}")
                        .collect(Collectors.joining(",", "[", "]"))
        );
        return false;
    }

}
