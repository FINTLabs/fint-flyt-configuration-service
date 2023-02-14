package no.fintlabs.validation.parsability.validators

import spock.lang.Specification

class FileParsabilityValidatorSpec extends Specification {
    FileParsabilityValidator fileParsabilityValidator

    def setup() {
        fileParsabilityValidator = new FileParsabilityValidator()
    }

    def 'Should return true for string that contains just one complete instance field reference'() {
        when:
        boolean valid = fileParsabilityValidator.isValid(
                "\$if{fil}"
        )
        then:
        valid
    }

    def 'Should return true for string that contains just one complete instance field reference that is empty'() {
        when:
        boolean valid = fileParsabilityValidator.isValid(
                "\$if{}"
        )
        then:
        valid
    }

    def 'Should return true for null value'(){
        when:
        boolean valid = fileParsabilityValidator.isValid(
                "null"
        )
        then:
        valid
    }

    def 'Should return false for string that contains incomplete instance field reference'() {
        when:
        boolean valid = fileParsabilityValidator.isValid(
                "\$if{fil"
        )
        then:
        !valid
    }

    def 'Should return false for empty string'() {
        when:
        boolean valid = fileParsabilityValidator.isValid(
                ""
        )
        then:
        !valid
    }

    def 'Should return false for string that has extra characters at the end of a complete instance field reference'() {
        when:
        boolean valid = fileParsabilityValidator.isValid(
                "\$if{fil} "
        )
        then:
        !valid
    }

    def 'Should return false for string that has extra characters at the beginning of a complete instance field reference'() {
        when:
        boolean valid = fileParsabilityValidator.isValid(
                " \$if{fil}"
        )
        then:
        !valid
    }
}
