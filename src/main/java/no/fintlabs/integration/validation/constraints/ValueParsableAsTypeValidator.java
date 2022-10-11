package no.fintlabs.integration.validation.constraints;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static no.fintlabs.integration.validation.constraints.ValueParsableAsType.FIELD_VALUE_TYPE_REF;

public abstract class ValueParsableAsTypeValidator<T> implements ConstraintValidator<ValueParsableAsType, T> {

    @Override
    public boolean isValid(T value, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = isValid(value);
        if (valid) {
            return true;
        }
        if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
            constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)
                    .addMessageParameter(FIELD_VALUE_TYPE_REF, getType(value));
        }
        return false;
    }

    abstract String getType(T value);

    abstract boolean isValid(T value);

}
