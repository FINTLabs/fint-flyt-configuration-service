package no.fintlabs.integration.validation.parsability.validators;

import no.fintlabs.integration.model.configuration.FieldConfiguration;
import no.fintlabs.integration.validation.ConfigurationValidationContext;
import no.fintlabs.integration.validation.parsability.FieldParsabilityValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(0)
public class BooleanParsabilityValidator implements FieldParsabilityValidator {

    @Override
    public FieldConfiguration.Type getTypeToValidate() {
        return FieldConfiguration.Type.BOOLEAN;
    }

    @Override
    public boolean isValid(String value, ConfigurationValidationContext configurationValidationContext) {
        return value.equals("true") || value.equals("false");
    }

}
