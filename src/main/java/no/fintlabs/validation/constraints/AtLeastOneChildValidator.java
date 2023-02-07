package no.fintlabs.validation.constraints;

import no.fintlabs.model.configuration.dtos.ElementMappingDto;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

public class AtLeastOneChildValidator implements HibernateConstraintValidator<AtLeastOneChild, ElementMappingDto> {

    @Override
    public boolean isValid(ElementMappingDto value, HibernateConstraintValidatorContext hibernateConstraintValidatorContext) {
        return value.getValueMappingPerKey().size() > 0
                || value.getElementMappingPerKey().size() > 0
                || value.getElementCollectionMappingPerKey().size() > 0;
    }

}
