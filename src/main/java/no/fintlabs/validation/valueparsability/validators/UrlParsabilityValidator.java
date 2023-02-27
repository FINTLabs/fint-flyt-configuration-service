package no.fintlabs.validation.valueparsability.validators;

import no.fintlabs.model.configuration.entities.value.ValueMapping;
import no.fintlabs.validation.valueparsability.ValueParsabilityValidator;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class UrlParsabilityValidator implements ValueParsabilityValidator {

    @Override
    public ValueMapping.Type getTypeToValidate() {
        return ValueMapping.Type.URL;
    }

    @Override
    public boolean isValid(String value) {
        try {
            new URL(value).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
