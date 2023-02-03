package no.fintlabs.validation.parsability.validators;

import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.validation.parsability.FieldParsabilityValidator;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class UrlParsabilityValidator implements FieldParsabilityValidator {

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
