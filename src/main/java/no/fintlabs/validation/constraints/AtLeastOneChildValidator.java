package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.ObjectMappingDto;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

public class AtLeastOneChildValidator implements HibernateConstraintValidator<AtLeastOneChild, ObjectMappingDto> {

    @Override
    public boolean isValid(ObjectMappingDto value, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
        return value.getValueMappingPerKey().size() > 0
                || value.getObjectMappingPerKey().size() > 0
                || value.getObjectCollectionMappingPerKey().size() > 0;
    }

}
