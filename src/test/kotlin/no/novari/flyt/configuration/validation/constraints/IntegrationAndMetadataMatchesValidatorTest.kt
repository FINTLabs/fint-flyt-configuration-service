package no.novari.flyt.configuration.validation.constraints

import no.novari.flyt.configuration.model.configuration.entities.Configuration
import no.novari.flyt.configuration.model.integration.Integration
import no.novari.flyt.configuration.model.metadata.IntegrationMetadata
import no.novari.flyt.configuration.validation.ConfigurationValidationContext
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class IntegrationAndMetadataMatchesValidatorTest {
    private lateinit var validator: IntegrationAndMetadataMatchesValidator

    @BeforeEach
    fun setup() {
        validator = IntegrationAndMetadataMatchesValidator()
    }

    @ParameterizedTest
    @CsvSource(
        "1,10,1,10,true",
        "1,10,2,10,false",
        "1,10,1,11,false",
        "1,10,2,11,false",
    )
    fun shouldReturnTrueOnlyWhenBothIdsMatch(
        integrationSaId: Long,
        integrationSaiId: String,
        metadataSaId: Long,
        metadataSaiId: String,
        shouldBeValid: Boolean,
    ) {
        val context =
            ConfigurationValidationContext
                .builder()
                .integration(
                    Integration
                        .builder()
                        .sourceApplicationId(integrationSaId)
                        .sourceApplicationIntegrationId(integrationSaiId)
                        .build(),
                ).metadata(
                    IntegrationMetadata
                        .builder()
                        .sourceApplicationId(metadataSaId)
                        .sourceApplicationIntegrationId(metadataSaiId)
                        .build(),
                ).build()

        val hibernateContext: HibernateConstraintValidatorContext = mock()
        whenever(hibernateContext.getConstraintValidatorPayload(ConfigurationValidationContext::class.java))
            .thenReturn(context)

        val valid = validator.isValid(Configuration(), hibernateContext)

        assertEquals(shouldBeValid, valid)
    }
}
