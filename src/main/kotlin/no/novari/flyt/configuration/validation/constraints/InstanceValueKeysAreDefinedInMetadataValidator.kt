package no.novari.flyt.configuration.validation.constraints

import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto
import no.novari.flyt.configuration.validation.ConfigurationValidationContext
import no.novari.flyt.configuration.validation.instancereference.InstanceValueKeyExtractionService
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import org.springframework.stereotype.Service

@Service
class InstanceValueKeysAreDefinedInMetadataValidator(
    private val instanceValueKeyExtractionService: InstanceValueKeyExtractionService,
) : HibernateConstraintValidator<InstanceValueKeysAreDefinedInMetadata, ValueMappingDto> {
    override fun isValid(
        value: ValueMappingDto,
        hibernateConstraintValidatorContext: HibernateConstraintValidatorContext,
    ): Boolean {
        val referencedInstanceValueKeys =
            instanceValueKeyExtractionService.extractIfReferenceKeys(value.mappingString)

        val metadataInstanceValueKeySet =
            hibernateConstraintValidatorContext
                .getConstraintValidatorPayload(ConfigurationValidationContext::class.java)
                .instanceValueTypePerKey
                .keys

        val keysMissingInMetadata = referencedInstanceValueKeys.filterNot(metadataInstanceValueKeySet::contains)

        if (keysMissingInMetadata.isNotEmpty()) {
            hibernateConstraintValidatorContext.addMessageParameter(
                InstanceValueKeysAreDefinedInMetadata.KEYS_MISSING_IN_METADATA,
                keysMissingInMetadata,
            )
        }
        return keysMissingInMetadata.isEmpty()
    }
}
