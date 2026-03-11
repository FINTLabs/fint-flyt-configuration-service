package no.novari.flyt.configuration.validation.constraints

import no.novari.flyt.configuration.model.configuration.dtos.ObjectMappingDto
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext

class AtLeastOneChildValidator : HibernateConstraintValidator<AtLeastOneChild, ObjectMappingDto> {
    override fun isValid(
        value: ObjectMappingDto,
        hibernateConstraintValidatorContext: HibernateConstraintValidatorContext,
    ): Boolean {
        return value.valueMappingPerKey.isNotEmpty() ||
            value.valueCollectionMappingPerKey.isNotEmpty() ||
            value.objectMappingPerKey.isNotEmpty() ||
            value.objectCollectionMappingPerKey.isNotEmpty()
    }
}
