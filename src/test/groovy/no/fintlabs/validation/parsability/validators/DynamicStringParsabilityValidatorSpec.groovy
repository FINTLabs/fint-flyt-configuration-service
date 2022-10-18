package no.fintlabs.validation.parsability.validators


import spock.lang.Specification

class DynamicStringParsabilityValidatorSpec extends Specification {

    DynamicStringParsabilityValidator dynamicStringParsabilityValidator

    def setup() {
        dynamicStringParsabilityValidator = new DynamicStringParsabilityValidator()
    }

    def 'Should return true for empty string'() {
        when:
        boolean valid = dynamicStringParsabilityValidator.isValid(
                ""
        )
        then:
        valid
    }

    def 'Should return true for blank string'() {
        when:
        boolean valid = dynamicStringParsabilityValidator.isValid(
                " "
        )
        then:
        valid
    }

    def 'Should return true for string without dynamic values'() {
        when:
        boolean valid = dynamicStringParsabilityValidator.isValid(
                "Søknad VGS"
        )
        then:
        valid
    }

    def 'Should return true for string with dynamic values of correct syntax'() {
        when:
        boolean valid = dynamicStringParsabilityValidator.isValid(
                "Søknad VGS \$if{fornavn}\$if{etter-navn} \$if{person nr1 fødselsdato} for dato \$if{dato} ettellerannet"
        )
        then:
        valid
    }

    def 'Should return true for string containing "$","{", "}" and "$if" that is not part of a instance field reference characters'() {
        when:
        boolean valid = dynamicStringParsabilityValidator.isValid(
                "Søknad VGS \$ { } \$if"
        )
        then:
        valid
    }

    def 'Should return false for string containing "$if{" that is not part of a complete instance field reference'() {
        when:
        boolean valid = dynamicStringParsabilityValidator.isValid(
                "Søknad VGS \$if{fornavn"
        )
        then:
        !valid
    }

    def 'Should return false for string containing empty instance field reference'() {
        when:
        boolean valid = dynamicStringParsabilityValidator.isValid(
                "Søknad VGS \$if{}"
        )
        then:
        !valid
    }

    def 'Should return false for string containing instance field reference inside instance field reference'() {
        when:
        boolean valid = dynamicStringParsabilityValidator.isValid(
                "Søknad VGS \$if{abc\$if{123}}"
        )
        then:
        !valid
    }

    def 'Should return false for string containing "$if{" inside instance field reference'() {
        when:
        boolean valid = dynamicStringParsabilityValidator.isValid(
                "Søknad VGS \$if{abc\$if{}"
        )
        then:
        !valid
    }

}
