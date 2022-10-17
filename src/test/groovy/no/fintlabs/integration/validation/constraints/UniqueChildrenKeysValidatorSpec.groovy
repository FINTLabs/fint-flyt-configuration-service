package no.fintlabs.integration.validation.constraints

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import org.hibernate.validator.internal.util.CollectionHelper
import spock.lang.Specification

import static no.fintlabs.integration.validation.constraints.UniqueChildrenKeys.DUPLICATE_CHILDREN_KEYS_REF

class UniqueChildrenKeysValidatorSpec extends Specification {

    UniqueChildrenKeysValidator uniqueChildrenKeysValidator
    HibernateConstraintValidatorContext constraintValidatorContext

    def setup() {
        uniqueChildrenKeysValidator = Spy(
                new UniqueChildrenKeysValidator() {
                    @Override
                    List<String> findDuplicateKeys(Object value) {
                        return null
                    }
                }
        )
        constraintValidatorContext = Mock(HibernateConstraintValidatorContext.class)
    }

    def 'should return true if no duplicate keys are found'() {
        given:
        Object object = new Object()

        when:
        boolean valid = uniqueChildrenKeysValidator.isValid(object, constraintValidatorContext)

        then:
        valid
        1 * uniqueChildrenKeysValidator.isValid(object, constraintValidatorContext)
        1 * uniqueChildrenKeysValidator.findDuplicateKeys(object) >> Collections.emptyList()
        0 * _
    }

    def 'should return false and add error message parameters if duplicate keys are found'() {
        given:
        Object object = new Object()
        List<String> duplicateKeys = List.of("one", "two")

        when:
        boolean valid = uniqueChildrenKeysValidator.isValid(object, constraintValidatorContext)

        then:
        !valid
        1 * uniqueChildrenKeysValidator.isValid(object, constraintValidatorContext)
        1 * uniqueChildrenKeysValidator.findDuplicateKeys(object) >> duplicateKeys
        1 * constraintValidatorContext.addMessageParameter(DUPLICATE_CHILDREN_KEYS_REF, "['one', 'two']") >> constraintValidatorContext
        1 * constraintValidatorContext.withDynamicPayload(CollectionHelper.toImmutableList(duplicateKeys))
        0 * _
    }

}
