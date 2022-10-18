package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.integration.Integration;
import no.fintlabs.integration.model.metadata.IntegrationMetadata;
import no.fintlabs.integration.validation.ConfigurationValidationContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import java.util.Objects;

public class IntegrationAndMetadataMatchesValidator implements
        HibernateConstraintValidator<IntegrationAndMetadataMatches, Object> {

    @Override
    public boolean isValid(Object value, HibernateConstraintValidatorContext context) {
        ConfigurationValidationContext configurationValidationContext =
                context.getConstraintValidatorPayload(ConfigurationValidationContext.class);

        Integration integration = configurationValidationContext.getIntegration();
        IntegrationMetadata metadata = configurationValidationContext.getMetadata();

        return Objects.equals(integration.getSourceApplicationId(), metadata.getSourceApplicationId())
                && Objects.equals(integration.getSourceApplicationIntegrationId(), metadata.getSourceApplicationIntegrationId());
    }

}
