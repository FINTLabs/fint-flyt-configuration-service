package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.entities.Configuration
import no.fintlabs.model.integration.Integration
import no.fintlabs.model.metadata.IntegrationMetadata
import no.fintlabs.validation.ConfigurationValidationContext
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import spock.lang.Specification

class IntegrationAndMetadataMatchesValidatorSpec extends Specification {

    IntegrationAndMetadataMatchesValidator integrationAndMetadataHasSameSourceApplicationIdAndSourceApplicationIntegrationIdValidator

    def setup() {
        integrationAndMetadataHasSameSourceApplicationIdAndSourceApplicationIntegrationIdValidator =
                new IntegrationAndMetadataMatchesValidator()
    }

    def 'Should return true only when both ids match'() {
        given:
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
                .build()
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = Mock(HibernateConstraintValidatorContext.class)
        hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class) >> configurationValidationContext

        when:
        boolean valid = integrationAndMetadataHasSameSourceApplicationIdAndSourceApplicationIntegrationIdValidator.isValid(
                new Configuration(),
                hibernateConstraintValidatorContext
        )

        then:
        valid == shouldBeValid

        where:
        integrationSaId | integrationSaiId || metadataSaId | metadataSaiId || shouldBeValid
        1               | "10"             || 1            | "10"          || true
        1               | "10"             || 2            | "10"          || false
        1               | "10"             || 1            | "11"          || false
        1               | "10"             || 2            | "11"          || false
    }


}
