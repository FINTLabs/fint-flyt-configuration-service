package no.novari.flyt.configuration.validation.constraints;

import no.novari.flyt.configuration.model.configuration.dtos.ObjectMappingDto;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

public class AtLeastOneChildValidator implements HibernateConstraintValidator<AtLeastOneChild, ObjectMappingDto> {

    @Override
    public boolean isValid(ObjectMappingDto value, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
        return !value.getValueMappingPerKey().isEmpty()
                || !value.getValueCollectionMappingPerKey().isEmpty()
                || !value.getObjectMappingPerKey().isEmpty()
                || !value.getObjectCollectionMappingPerKey().isEmpty();
    }

}
