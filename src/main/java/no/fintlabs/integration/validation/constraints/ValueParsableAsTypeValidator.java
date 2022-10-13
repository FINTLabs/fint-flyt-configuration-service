package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.validation.ConfigurationValidationContext;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static no.fintlabs.integration.validation.constraints.ValueParsableAsType.FIELD_VALUE_TYPE_REF;

public abstract class ValueParsableAsTypeValidator<T> implements ConstraintValidator<ValueParsableAsType, T> {

    @Override
    public boolean isValid(T value, ConstraintValidatorContext constraintValidatorContext) {
        if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
            HibernateConstraintValidatorContext hibernateConstraintValidatorContext =
                    constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);
            boolean valid = isValid(
                    value,
                    hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class)
            );
            if (valid) {
                return true;
            }
            hibernateConstraintValidatorContext.addMessageParameter(FIELD_VALUE_TYPE_REF, getType(value));
        }
        return false;
    }

    protected abstract String getType(T value);

    protected abstract boolean isValid(T value, ConfigurationValidationContext configurationValidationContext);

}
