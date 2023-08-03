package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.entities.Configuration;
import no.fintlabs.model.integration.Integration;
import no.fintlabs.model.metadata.IntegrationMetadata;
import no.fintlabs.validation.ConfigurationValidationContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IntegrationAndMetadataMatchesValidatorTest {

    private IntegrationAndMetadataMatchesValidator integrationAndMetadataHasSameSourceApplicationIdAndSourceApplicationIntegrationIdValidator;

    @BeforeEach
    void setup() {
        integrationAndMetadataHasSameSourceApplicationIdAndSourceApplicationIntegrationIdValidator =
                new IntegrationAndMetadataMatchesValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "1, '10', 1, '10', true",
            "1, '10', 2, '10', false",
            "1, '10', 1, '11', false",
            "1, '10', 2, '11', false"
    })
    void shouldReturnTrueOnlyWhenBothIdsMatch(Long integrationSaId, String integrationSaiId, Long metadataSaId, String metadataSaiId, boolean shouldBeValid) {
        ConfigurationValidationContext configurationValidationContext = ConfigurationValidationContext
                .builder()
                .integration(Integration
                        .builder()
                        .sourceApplicationId(integrationSaId)
                        .sourceApplicationIntegrationId(integrationSaiId)
                        .build()
                )
                .metadata(IntegrationMetadata
                        .builder()
                        .sourceApplicationId(metadataSaId)
                        .sourceApplicationIntegrationId(metadataSaiId)
                        .build()
                )
                .build();
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = mock(HibernateConstraintValidatorContext.class);
        when(hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class))
                .thenReturn(configurationValidationContext);

        boolean valid = integrationAndMetadataHasSameSourceApplicationIdAndSourceApplicationIntegrationIdValidator.isValid(
                new Configuration(),
                hibernateConstraintValidatorContext
        );

        assertEquals(shouldBeValid, valid);
    }
}
