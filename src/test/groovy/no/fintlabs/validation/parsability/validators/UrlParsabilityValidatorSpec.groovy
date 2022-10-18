package no.fintlabs.validation.parsability.validators

import spock.lang.Specification

class UrlParsabilityValidatorSpec extends Specification {

    UrlParsabilityValidator urlParsabilityValidator

    def setup() {
        urlParsabilityValidator = new UrlParsabilityValidator()
    }

    def 'should return true if value is parsable as URL'() {
        when:
        boolean valid = urlParsabilityValidator.isValid("http://www.example.com")

        then:
        valid
    }

    def 'should return false if value is not parsable as URL'() {
        when:
        boolean valid = urlParsabilityValidator.isValid("httpkk://www.example.com")

        then:
        !valid
    }

    def 'should return false if value is empty'() {
        when:
        boolean valid = urlParsabilityValidator.isValid("")

        then:
        !valid
    }

    def 'should return false if value is blank'() {
        when:
        boolean valid = urlParsabilityValidator.isValid(" ")

        then:
        !valid
    }

}
