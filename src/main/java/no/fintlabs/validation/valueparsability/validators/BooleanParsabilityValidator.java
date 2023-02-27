package no.fintlabs.validation.valueparsability.validators;

import no.fintlabs.model.configuration.entities.value.ValueMapping;
import no.fintlabs.validation.valueparsability.ValueParsabilityValidator;
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
