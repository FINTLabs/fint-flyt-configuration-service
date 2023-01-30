package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.ValueMappingDto;
import no.fintlabs.validation.parsability.FieldParsabilityValidator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static no.fintlabs.validation.constraints.ValueParsableAsType.FIELD_VALUE_TYPE_REF;

@Service
public class ValueParsableAsTypeValidator implements HibernateConstraintValidator<ValueParsableAsType, ValueMappingDto> {

    private final Collection<FieldParsabilityValidator> fieldParsabilityValidators;

    public ValueParsableAsTypeValidator(Collection<FieldParsabilityValidator> fieldParsabilityValidators) {
        this.fieldParsabilityValidators = fieldParsabilityValidators;
    }

    @Override
    public boolean isValid(ValueMappingDto value, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
        boolean valid = fieldParsabilityValidators
                .stream()
                .allMatch(fieldParsabilityValidator -> fieldParsabilityValidator.isValid(value));
        if (valid) {
            return true;
        }
        hibernateConstraintValidatorContext.addMessageParameter(FIELD_VALUE_TYPE_REF, value.getType().toString());
        return false;
    }

}
