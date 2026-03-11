package no.novari.flyt.configuration.validation.constraints

import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping
import no.novari.flyt.configuration.model.metadata.InstanceValueMetadata
import no.novari.flyt.configuration.validation.ConfigurationValidationContext
import no.novari.flyt.configuration.validation.instancereference.InstanceValueKeyExtractionService
import no.novari.flyt.configuration.validation.instancereference.InstanceValueTypeCompatibilityValidator
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.AbstractMap

class InstanceValueTypesAreCompatibleValidatorTest {
    private lateinit var instanceValueKeyExtractionService: InstanceValueKeyExtractionService
    private lateinit var instanceValueTypeCompatibilityValidators: MutableList<InstanceValueTypeCompatibilityValidator>
    private lateinit var hibernateConstraintValidatorContext: HibernateConstraintValidatorContext

    private lateinit var validator: InstanceValueTypesAreCompatibleValidator

    @BeforeEach
    fun setup() {
        instanceValueKeyExtractionService = mock()
        hibernateConstraintValidatorContext = mock()

        instanceValueTypeCompatibilityValidators = mutableListOf()
        val mockValidator = mock<InstanceValueTypeCompatibilityValidator>()
        instanceValueTypeCompatibilityValidators.add(mockValidator)

        validator =
            InstanceValueTypesAreCompatibleValidator(
                instanceValueKeyExtractionService,
                instanceValueTypeCompatibilityValidators,
            )
    }

    @Test
    fun testIsValidTrue() {
        val dto =
            ValueMappingDto
                .builder()
                .type(ValueMapping.Type.STRING)
                .mappingString("mapping")
                .build()
        val map = mutableMapOf("key" to InstanceValueMetadata.Type.STRING)
        val context = ConfigurationValidationContext.builder().instanceValueTypePerKey(map).build()

        whenever(instanceValueKeyExtractionService.extractIfReferenceKeys(any<String>())).thenReturn(listOf("key"))
        whenever(
            hibernateConstraintValidatorContext.getConstraintValidatorPayload(
                ConfigurationValidationContext::class.java,
            ),
        ).thenReturn(context)

        for (compatibilityValidator in instanceValueTypeCompatibilityValidators) {
            whenever(
                compatibilityValidator.findIncompatibleInstanceValuesKeyAndType(dto, listOf("key"), map),
            ).thenReturn(listOf())
        }

        val result = validator.isValid(dto, hibernateConstraintValidatorContext)
        assertTrue(result)
    }

    @Test
    fun testIsValidFalse() {
        val dto =
            ValueMappingDto
                .builder()
                .type(ValueMapping.Type.STRING)
                .mappingString("mapping")
                .build()
        val map = mutableMapOf("key" to InstanceValueMetadata.Type.FILE)
        val context = ConfigurationValidationContext.builder().instanceValueTypePerKey(map).build()

        whenever(instanceValueKeyExtractionService.extractIfReferenceKeys(any<String>())).thenReturn(listOf("key"))
        whenever(
            hibernateConstraintValidatorContext.getConstraintValidatorPayload(
                ConfigurationValidationContext::class.java,
            ),
        ).thenReturn(context)

        for (compatibilityValidator in instanceValueTypeCompatibilityValidators) {
            whenever(compatibilityValidator.findIncompatibleInstanceValuesKeyAndType(dto, listOf("key"), map))
                .thenReturn(listOf(AbstractMap.SimpleEntry("key", InstanceValueMetadata.Type.FILE)))
        }

        val result = validator.isValid(dto, hibernateConstraintValidatorContext)
        assertFalse(result)
    }
}
