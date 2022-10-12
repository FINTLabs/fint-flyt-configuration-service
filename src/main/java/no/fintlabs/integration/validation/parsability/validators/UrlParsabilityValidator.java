package no.fintlabs.integration.validation.parsability.validators;

import no.fintlabs.integration.model.CollectionFieldConfiguration;
import no.fintlabs.integration.model.FieldConfiguration;
import no.fintlabs.integration.validation.parsability.CollectionFieldParsabilityValidator;
import no.fintlabs.integration.validation.parsability.FieldParsabilityValidator;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Collection;

@Service
public class UrlParsabilityValidator implements FieldParsabilityValidator, CollectionFieldParsabilityValidator {

    @Override
    public FieldConfiguration.Type getTypeToValidate() {
        return FieldConfiguration.Type.URL;
    }

    @Override
    public CollectionFieldConfiguration.Type getCollectionFieldType() {
        return CollectionFieldConfiguration.Type.URL;
    }

    @Override
    public boolean isValid(Collection<String> values) {
        return values.stream().allMatch(this::isValid);
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
