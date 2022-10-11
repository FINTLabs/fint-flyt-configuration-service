package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.FieldConfiguration;
import no.fintlabs.integration.validation.parsability.FieldParsabilityValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ValueParsableAsTypeFieldConfigurationValidator extends ValueParsableAsTypeValidator<FieldConfiguration> {

    private final Collection<FieldParsabilityValidator> fieldParsabilityValidators;

    public ValueParsableAsTypeFieldConfigurationValidator(Collection<FieldParsabilityValidator> fieldParsabilityValidators) {
        this.fieldParsabilityValidators = fieldParsabilityValidators;
    }

    @Override
    String getType(FieldConfiguration value) {
        return value.getType().toString();
    }

    @Override
    public boolean isValid(FieldConfiguration value) {
        return fieldParsabilityValidators
                .stream()
                .allMatch(fieldParsabilityValidator -> fieldParsabilityValidator.isValid(value));
    }

}
