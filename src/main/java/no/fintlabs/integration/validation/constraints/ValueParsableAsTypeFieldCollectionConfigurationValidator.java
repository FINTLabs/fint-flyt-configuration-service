package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.FieldCollectionConfiguration;
import no.fintlabs.integration.validation.parsability.FieldCollectionParsabilityValidator;

import java.util.Collection;

public class ValueParsableAsTypeFieldCollectionConfigurationValidator extends ValueParsableAsTypeValidator<FieldCollectionConfiguration> {

    private final Collection<FieldCollectionParsabilityValidator> fieldCollectionParsabilityValidators;

    public ValueParsableAsTypeFieldCollectionConfigurationValidator(
            Collection<FieldCollectionParsabilityValidator> fieldCollectionParsabilityValidators
    ) {
        this.fieldCollectionParsabilityValidators = fieldCollectionParsabilityValidators;
    }

    @Override
    String getType(FieldCollectionConfiguration value) {
        return value.getType().toString();
    }

    @Override
    public boolean isValid(FieldCollectionConfiguration value) {
        return fieldCollectionParsabilityValidators
                .stream()
                .allMatch(fieldParsabilityValidator -> fieldParsabilityValidator.isValid(value));
    }

}
