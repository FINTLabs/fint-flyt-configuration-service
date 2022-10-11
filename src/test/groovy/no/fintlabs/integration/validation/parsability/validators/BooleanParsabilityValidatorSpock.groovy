package no.fintlabs.integration.validation.parsability.validators

import spock.lang.Specification

class BooleanParsabilityValidatorSpock extends Specification {

    BooleanParsabilityValidator booleanParsabilityValidator

    def setup() {
        booleanParsabilityValidator = new BooleanParsabilityValidator()
    }

    def 'should return true if value is "true"'() {
        when:
        boolean valid = booleanParsabilityValidator.isValid("true")

        then:
        valid
    }

    def 'should return true if value is "false"'() {
        when:
        boolean valid = booleanParsabilityValidator.isValid("false")

        then:
        valid
    }

    def 'should return false if value is empty'() {
        when:
        boolean valid = booleanParsabilityValidator.isValid("")

        then:
        !valid
    }

    def 'should return false if value is blank'() {
        when:
        boolean valid = booleanParsabilityValidator.isValid(" ")

        then:
        !valid
    }

    def 'should return false if value is "truest"'() {
        when:
        boolean valid = booleanParsabilityValidator.isValid("truest")

        then:
        !valid
    }

    def 'should return true if value is list of parsable values'() {
        given:
        List<String> values = List.of("false", "true", "true")

        when:
        boolean valid = booleanParsabilityValidator.isValid(values)

        then:
        valid
    }

    def 'should return true if value is empty list '() {
        given:
        List<String> values = List.of()

        when:
        boolean valid = booleanParsabilityValidator.isValid(values)

        then:
        valid
    }

    def 'should return false if value is list contains one unparsable value'() {
        given:
        List<String> values = List.of("false", "true", "truest")

        when:
        boolean valid = booleanParsabilityValidator.isValid(values)

        then:
        !valid
    }

}
