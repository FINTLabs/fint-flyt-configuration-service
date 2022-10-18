package no.fintlabs.validation.parsability.validators;

import no.fintlabs.model.configuration.entities.FieldConfiguration;
import no.fintlabs.validation.parsability.FieldParsabilityValidator;
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
