package no.fintlabs.validation.parsability.validators;

import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.validation.parsability.FieldParsabilityValidator;
import org.springframework.stereotype.Service;

@Service
public class BooleanParsabilityValidator implements FieldParsabilityValidator {

    @Override
    public ValueMapping.Type getTypeToValidate() {
        return ValueMapping.Type.BOOLEAN;
    }

    @Override
    public boolean isValid(String value) {
        return value.equals("true") || value.equals("false");
    }

}
