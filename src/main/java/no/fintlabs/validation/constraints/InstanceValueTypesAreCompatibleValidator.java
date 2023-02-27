package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.value.ValueMappingDto;
import no.fintlabs.model.metadata.InstanceValueMetadata;
import no.fintlabs.validation.ConfigurationValidationContext;
import no.fintlabs.validation.instancereference.InstanceValueKeyExtractionService;
import no.fintlabs.validation.instancereference.InstanceValueTypeCompatibilityValidator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.stereotype.Service;
import reactor.util.function.Tuple2;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static no.fintlabs.validation.constraints.InstanceValueTypesAreCompatible.CONFIGURATION_VALUE_TYPE;
import static no.fintlabs.validation.constraints.InstanceValueTypesAreCompatible.INCOMPATIBLE_INSTANCE_VALUES_KEY_AND_TYPE;

@Service
public class InstanceValueTypesAreCompatibleValidator implements
        HibernateConstraintValidator<InstanceValueTypesAreCompatible, ValueMappingDto> {

    private final InstanceValueKeyExtractionService instanceValueKeyExtractionService;
    private final Collection<InstanceValueTypeCompatibilityValidator> instanceValueTypeCompatibilityValidators;

    public InstanceValueTypesAreCompatibleValidator(
            InstanceValueKeyExtractionService instanceValueKeyExtractionService,
            Collection<InstanceValueTypeCompatibilityValidator> instanceValueTypeCompatibilityValidators
    ) {
        this.instanceValueKeyExtractionService = instanceValueKeyExtractionService;
        this.instanceValueTypeCompatibilityValidators = instanceValueTypeCompatibilityValidators;
    }

    @Override
    public boolean isValid(ValueMappingDto valueMappingDto, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
        Collection<String> referencedInstanceValueKeys = instanceValueKeyExtractionService
                .extractIfReferenceKeys(valueMappingDto.getMappingString());

        Map<String, InstanceValueMetadata.Type> metadataInstanceValueTypePerKey = hibernateConstraintValidatorContext
                .getConstraintValidatorPayload(ConfigurationValidationContext.class)
                .getInstanceValueTypePerKey();

        List<Tuple2<String, InstanceValueMetadata.Type>> incompatibleInstanceValuesKeyAndType = instanceValueTypeCompatibilityValidators
                .stream()
                .map(instanceValueTypeCompatibilityValidator -> instanceValueTypeCompatibilityValidator
                        .findIncompatibleInstanceValuesKeyAndType(
                                valueMappingDto,
                                referencedInstanceValueKeys,
                                metadataInstanceValueTypePerKey
                        )
                ).flatMap(Collection::stream)
                .toList();

        if (incompatibleInstanceValuesKeyAndType.isEmpty()) {
            return true;
        }

        hibernateConstraintValidatorContext.addMessageParameter(CONFIGURATION_VALUE_TYPE, valueMappingDto.getType().name());
        hibernateConstraintValidatorContext.addMessageParameter(
                INCOMPATIBLE_INSTANCE_VALUES_KEY_AND_TYPE,
                incompatibleInstanceValuesKeyAndType.stream()
                        .map(tuple -> "{key=" + tuple.getT1() + ", type=" + tuple.getT2() + "}")
                        .collect(Collectors.joining(",", "[", "]"))
        );
        return false;
    }

}
