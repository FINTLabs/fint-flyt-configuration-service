package no.novari.configuration.validation.valueparsability.validators;

import no.novari.configuration.model.configuration.entities.ValueMapping;
import no.novari.configuration.validation.valueparsability.ValueParsabilityValidator;
import org.springframework.stereotype.Service;

@Service
public class BooleanParsabilityValidator implements ValueParsabilityValidator {

    @Override
    public ValueMapping.Type getTypeToValidate() {
        return ValueMapping.Type.BOOLEAN;
    }

    @Override
    public boolean isValid(String value) {
        return value.equals("true") || value.equals("false");
    }

}
