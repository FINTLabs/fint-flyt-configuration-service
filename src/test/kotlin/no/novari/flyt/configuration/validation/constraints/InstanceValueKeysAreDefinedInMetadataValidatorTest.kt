package no.novari.flyt.configuration.validation.constraints

import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping
import no.novari.flyt.configuration.model.metadata.InstanceValueMetadata
import no.novari.flyt.configuration.validation.ConfigurationValidationContext
import no.novari.flyt.configuration.validation.instancereference.InstanceValueKeyExtractionService
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class InstanceValueKeysAreDefinedInMetadataValidatorTest {
    private lateinit var validator: InstanceValueKeysAreDefinedInMetadataValidator

    @BeforeEach
    fun setup() {
        validator = InstanceValueKeysAreDefinedInMetadataValidator(InstanceValueKeyExtractionService())
    }

    @Test
    fun shouldReturnTrueWhenValueContainsNoInstanceFieldReferences() {
        val context =
            ConfigurationValidationContext
                .builder()
                .instanceValueTypePerKey(
                    mapOf(
                        "instanceValueKey1" to InstanceValueMetadata.Type.STRING,
                        "instanceValueKey2" to InstanceValueMetadata.Type.STRING,
                        "instanceValueKey3" to InstanceValueMetadata.Type.STRING,
                    ),
                ).build()
        val hibernateContext: HibernateConstraintValidatorContext = mock()
        whenever(hibernateContext.getConstraintValidatorPayload(ConfigurationValidationContext::class.java))
            .thenReturn(context)

        val valueMappingDto =
            ValueMappingDto
                .builder()
                .type(
                    ValueMapping.Type.DYNAMIC_STRING,
                ).mappingString("Title")
                .build()

        val valid = validator.isValid(valueMappingDto, hibernateContext)

        assertTrue(valid)
    }

    @Test
    fun shouldReturnTrueWhenAllInstanceFieldReferenceKeysAreFoundInMetadata() {
        val context =
            ConfigurationValidationContext
                .builder()
                .instanceValueTypePerKey(
                    mapOf(
                        "instanceValueKey1" to InstanceValueMetadata.Type.STRING,
                        "instanceValueKey2" to InstanceValueMetadata.Type.STRING,
                        "instanceValueKey3" to InstanceValueMetadata.Type.STRING,
                    ),
                ).build()
        val hibernateContext: HibernateConstraintValidatorContext = mock()
        whenever(hibernateContext.getConstraintValidatorPayload(ConfigurationValidationContext::class.java))
            .thenReturn(context)

        val valueMappingDto =
            ValueMappingDto
                .builder()
                .type(ValueMapping.Type.DYNAMIC_STRING)
                .mappingString("Title \$if{instanceValueKey1} \$if{instanceValueKey2}")
                .build()

        val valid = validator.isValid(valueMappingDto, hibernateContext)

        assertTrue(valid)
    }

    @Test
    fun shouldReturnFalseWhenAnInstanceFieldReferenceKeyIsNotFoundInMetadata() {
        val context =
            ConfigurationValidationContext
                .builder()
                .instanceValueTypePerKey(
                    mapOf(
                        "instanceValueKey1" to InstanceValueMetadata.Type.STRING,
                        "instanceValueKey3" to InstanceValueMetadata.Type.STRING,
                    ),
                ).build()
        val hibernateContext: HibernateConstraintValidatorContext = mock()
        whenever(hibernateContext.getConstraintValidatorPayload(ConfigurationValidationContext::class.java))
            .thenReturn(context)

        val valueMappingDto =
            ValueMappingDto
                .builder()
                .type(ValueMapping.Type.DYNAMIC_STRING)
                .mappingString("Title \$if{instanceValueKey1} \$if{instanceValueKey2}")
                .build()

        val valid = validator.isValid(valueMappingDto, hibernateContext)

        assertFalse(valid)
    }
}
