package no.fintlabs.integration.validation.parsability.validators;

import no.fintlabs.integration.model.FieldConfiguration;
import no.fintlabs.integration.validation.parsability.FieldParsabilityValidator;
import org.springframework.stereotype.Service;

@Service
public class BooleanParsabilityValidator implements FieldParsabilityValidator {

    @Override
    public FieldConfiguration.Type getTypeToValidate() {
        return FieldConfiguration.Type.BOOLEAN;
    }

    @Override
    public boolean isValid(String value) {
        return value.equals("true") || value.equals("false");
    }

}
