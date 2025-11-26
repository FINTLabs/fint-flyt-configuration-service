package no.novari.flyt.configuration.validation.valueparsability.validators;

import no.novari.flyt.configuration.model.configuration.entities.ValueMapping;
import no.novari.flyt.configuration.validation.valueparsability.ValueParsabilityValidator;
import org.springframework.stereotype.Service;

import java.net.URI;
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
            URI uri = URI.create(value);
            URL ignored = uri.toURL();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
