package no.fintlabs.integration.validation.parsability.validators;

import no.fintlabs.integration.model.FieldCollectionConfiguration;
import no.fintlabs.integration.model.FieldConfiguration;
import no.fintlabs.integration.validation.parsability.FieldCollectionParsabilityValidator;
import no.fintlabs.integration.validation.parsability.FieldParsabilityValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BooleanParsabilityValidator implements FieldParsabilityValidator, FieldCollectionParsabilityValidator {

    @Override
    public FieldConfiguration.Type getFieldValueType() {
        return FieldConfiguration.Type.BOOLEAN;
    }

    @Override
    public FieldCollectionConfiguration.Type getFieldCollectionType() {
        return FieldCollectionConfiguration.Type.BOOLEAN;
    }

    @Override
    public boolean isValid(String value) {
        return value.equals("true") || value.equals("false");
    }

    @Override
    public boolean isValid(Collection<String> values) {
        return values.stream().allMatch(this::isValid);
    }

}
