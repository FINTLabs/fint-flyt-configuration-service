package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.FieldConfigurationDto;
import no.fintlabs.validation.parsability.FieldParsabilityValidator;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ValueParsableAsTypeFieldConfigurationValidator extends ValueParsableAsTypeValidator<FieldConfigurationDto> {

    private final Collection<FieldParsabilityValidator> fieldParsabilityValidators;

    public ValueParsableAsTypeFieldConfigurationValidator(Collection<FieldParsabilityValidator> fieldParsabilityValidators) {
        this.fieldParsabilityValidators = fieldParsabilityValidators;
    }

    @Override
    protected String getType(FieldConfigurationDto value) {
        return value.getType().toString();
    }

    @Override
    protected boolean isValid(FieldConfigurationDto value) {
        return fieldParsabilityValidators
                .stream()
                .allMatch(fieldParsabilityValidator -> fieldParsabilityValidator.isValid(value));
    }

}