package no.novari.configuration.validation.constraints;

import no.novari.configuration.model.configuration.dtos.ValueMappingDto;
import no.novari.configuration.validation.valueparsability.ValueParsabilityValidator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static no.novari.configuration.validation.constraints.ValueParsableAsType.VALUE_TYPE;

@Service
public class ValueParsableAsTypeValidator implements HibernateConstraintValidator<ValueParsableAsType, ValueMappingDto> {

    private final Collection<ValueParsabilityValidator> valueParsabilityValidators;

    public ValueParsableAsTypeValidator(Collection<ValueParsabilityValidator> valueParsabilityValidators) {
        this.valueParsabilityValidators = valueParsabilityValidators;
    }

    @Override
    public boolean isValid(ValueMappingDto value, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
        boolean valid = valueParsabilityValidators
                .stream()
                .allMatch(valueParsabilityValidator -> valueParsabilityValidator.isValid(value));
        if (valid) {
            return true;
        }
        hibernateConstraintValidatorContext.addMessageParameter(VALUE_TYPE, value.getType().toString());
        return false;
    }

}
