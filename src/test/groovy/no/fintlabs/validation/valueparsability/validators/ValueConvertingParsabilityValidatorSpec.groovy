package no.fintlabs.validation.valueparsability.validators

import spock.lang.Specification

class ValueConvertingParsabilityValidatorSpec extends Specification {

    ValueConvertingParsabilityValidator valueConvertingParsabilityValidator

    def setup() {
        valueConvertingParsabilityValidator = new ValueConvertingParsabilityValidator()
    }

    def 'Should return true for string with value converting and instance field reference of correct syntax'() {
        when:
        boolean valid = valueConvertingParsabilityValidator.isValid(
                "\$vc{0}\$if{fornavn}"
        )
        then:
        valid
    }

    def 'Should return true for string with value converting and instance collection field reference of correct syntax'() {
        when:
        boolean valid = valueConvertingParsabilityValidator.isValid(
                "\$vc{0}\$icf{0}{fornavn}"
        )
        then:
        valid
    }

    def 'Should return false for empty string'() {
        when:
        boolean valid = valueConvertingParsabilityValidator.isValid("")
        then:
        !valid
    }

    def 'Should return false for blank string'() {
        when:
        boolean valid = valueConvertingParsabilityValidator.isValid(" ")
        then:
        !valid
    }

    def 'Should return false for string without dynamic values'() {
        when:
        boolean valid = valueConvertingParsabilityValidator.isValid("Søknad VGS")
        then:
        !valid
    }

    def 'Should return false for string with value converting with wrong syntax'() {
        when:
        boolean valid = valueConvertingParsabilityValidator.isValid(
                "asd\$if{fornavn}"
        )
        then:
        !valid
    }

    def 'Should return false for string with value converting with string id'() {
        when:
        boolean valid = valueConvertingParsabilityValidator.isValid(
                "\$vc(asd)\$if{fornavn}"
        )
        then:
        !valid
    }

    def 'Should return false for string with value converter reference last'() {
        when:
        boolean valid = valueConvertingParsabilityValidator.isValid(
                "\$if{fornavn}\$vc(asd)"
        )
        then:
        !valid
    }

    def 'Should return false for string without value converter reference'() {
        when:
        boolean valid = valueConvertingParsabilityValidator.isValid(
                "\$if{fornavn}"
        )
        then:
        !valid
    }

    def 'Should return false for string without instance field reference'() {
        when:
        boolean valid = valueConvertingParsabilityValidator.isValid(
                "\$if{fornavn}"
        )
        then:
        !valid
    }

    def 'Should return false for string with value converting and instance field reference of correct syntax but additional text'() {
        when:
        boolean valid = valueConvertingParsabilityValidator.isValid(
                "\$vc(0)a\$if{fornavn}"
        )
        then:
        !valid
    }

}
