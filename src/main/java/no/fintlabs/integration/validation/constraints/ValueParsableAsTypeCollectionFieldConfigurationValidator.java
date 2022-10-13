package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.configuration.CollectionFieldConfiguration;
import no.fintlabs.integration.validation.parsability.CollectionFieldParsabilityValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
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
