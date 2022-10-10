package no.fintlabs.integration.validation.constraints;

import no.fintlabs.integration.model.FieldConfiguration;
import no.fintlabs.integration.validation.parsability.FieldParsabilityValidator;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

import static no.fintlabs.integration.validation.constraints.ValueParsableAsType.FIELD_VALUE_TYPE_REF;

@Service
public class ValueParsableAsTypeFieldConfigurationValidator implements ConstraintValidator<ValueParsableAsType, FieldConfiguration> {

    private final Collection<FieldParsabilityValidator> fieldParsabilityValidators;

    public ValueParsableAsTypeFieldConfigurationValidator(Collection<FieldParsabilityValidator> fieldParsabilityValidators) {
        this.fieldParsabilityValidators = fieldParsabilityValidators;
    }

    @Override
    public boolean isValid(FieldConfiguration fieldConfiguration, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = fieldParsabilityValidators
                .stream()
                .allMatch(fieldParsabilityValidator -> fieldParsabilityValidator.validate(fieldConfiguration));
        if (valid) {
            return true;
        }
        if (constraintValidatorContext instanceof HibernateConstraintValidatorContext) {
            constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class)
                    .addMessageParameter(FIELD_VALUE_TYPE_REF, fieldConfiguration.getType());
        }
        return false;
    }

}
