package no.fintlabs.validation.constraints;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import static no.fintlabs.validation.constraints.ValueParsableAsType.FIELD_VALUE_TYPE_REF;

public abstract class ValueParsableAsTypeValidator<T> implements HibernateConstraintValidator<ValueParsableAsType, T> {

    @Override
    public boolean isValid(T value, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
        boolean valid = isValid(value);
        if (valid) {
            return true;
        }
        hibernateConstraintValidatorContext.addMessageParameter(FIELD_VALUE_TYPE_REF, getType(value));
        return false;
    }

    protected abstract String getType(T value);

    protected abstract boolean isValid(T value);

}
