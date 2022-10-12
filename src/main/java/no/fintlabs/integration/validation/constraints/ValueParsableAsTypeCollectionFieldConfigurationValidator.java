package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.CollectionFieldConfiguration;
import no.fintlabs.integration.validation.parsability.CollectionFieldParsabilityValidator;

import java.util.Collection;

public class ValueParsableAsTypeCollectionFieldConfigurationValidator extends ValueParsableAsTypeValidator<CollectionFieldConfiguration> {

    private final Collection<CollectionFieldParsabilityValidator> collectionFieldParsabilityValidators;

    public ValueParsableAsTypeCollectionFieldConfigurationValidator(
            Collection<CollectionFieldParsabilityValidator> collectionFieldParsabilityValidators
    ) {
        this.collectionFieldParsabilityValidators = collectionFieldParsabilityValidators;
    }

    @Override
    protected String getType(CollectionFieldConfiguration value) {
        return value.getType().toString();
    }

    @Override
    protected boolean isValid(CollectionFieldConfiguration value) {
        return collectionFieldParsabilityValidators
                .stream()
                .allMatch(fieldParsabilityValidator -> fieldParsabilityValidator.isValid(value));
    }

}
