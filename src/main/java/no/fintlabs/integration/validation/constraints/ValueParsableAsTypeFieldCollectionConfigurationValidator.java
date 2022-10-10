package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.FieldCollectionConfiguration;
import no.fintlabs.integration.validation.parsability.FieldCollectionParsabilityValidator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

import static no.fintlabs.integration.validation.constraints.ValueParsableAsType.FIELD_VALUE_TYPE_REF;

public class ValueParsableAsTypeFieldCollectionConfigurationValidator implements ConstraintValidator<ValueParsableAsType, FieldCollectionConfiguration> {

    private final Collection<FieldCollectionParsabilityValidator> fieldCollectionParsabilityValidators;

    public ValueParsableAsTypeFieldCollectionConfigurationValidator(
            Collection<FieldCollectionParsabilityValidator> fieldCollectionParsabilityValidators
    ) {
        this.fieldCollectionParsabilityValidators = fieldCollectionParsabilityValidators;
    }


    @Override
    public boolean isValid(FieldCollectionConfiguration fieldCollectionConfiguration, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = fieldCollectionParsabilityValidators
                .stream()
                .allMatch(validator -> validator.validate(fieldCollectionConfiguration));
        if (valid) {
            return true;
        }
        if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
            constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)
                    .addMessageParameter(FIELD_VALUE_TYPE_REF, fieldCollectionConfiguration.getType());
        }
        return false;
    }

}
