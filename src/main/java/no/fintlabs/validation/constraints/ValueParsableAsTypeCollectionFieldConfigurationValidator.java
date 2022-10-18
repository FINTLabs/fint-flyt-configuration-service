package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.CollectionFieldConfigurationDto;
import no.fintlabs.validation.parsability.CollectionFieldParsabilityValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ValueParsableAsTypeCollectionFieldConfigurationValidator extends ValueParsableAsTypeValidator<CollectionFieldConfigurationDto> {

    private final Collection<CollectionFieldParsabilityValidator> collectionFieldParsabilityValidators;

    public ValueParsableAsTypeCollectionFieldConfigurationValidator(
            Collection<CollectionFieldParsabilityValidator> collectionFieldParsabilityValidators
    ) {
        this.collectionFieldParsabilityValidators = collectionFieldParsabilityValidators;
    }

    @Override
    protected String getType(CollectionFieldConfigurationDto value) {
        return value.getType().toString();
    }

    @Override
    protected boolean isValid(CollectionFieldConfigurationDto value) {
        return collectionFieldParsabilityValidators
                .stream()
                .allMatch(fieldParsabilityValidator -> fieldParsabilityValidator.isValid(value));
    }

}
