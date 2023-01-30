package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.dtos.ElementCollectionMappingDto
import no.fintlabs.model.configuration.dtos.ElementMappingDto
import no.fintlabs.model.configuration.dtos.ValueMappingDto
import spock.lang.Specification

class UniqueChildrenKeysValidatorSpec extends Specification {

    UniqueChildrenKeysValidator uniqueChildrenKeysValidator

    def setup() {
        uniqueChildrenKeysValidator = new UniqueChildrenKeysValidator()
    }

    def 'should return duplicate keys in a single collection and across child collections'() {
        given:
        ElementMappingDto elementMappingDto = ElementMappingDto
                .builder()
                .valueMappingPerKey(Map.of(
                        "one", ValueMappingDto.builder().build(),
                        "two", ValueMappingDto.builder().build(),
                        "six", ValueMappingDto.builder().build(),
                ))
                .elementMappingPerKey(Map.of(
                        "two", ElementMappingDto.builder().build(),
                        "three", ElementMappingDto.builder().build(),
                        "five", ElementMappingDto.builder().build(),
                        "four", ElementMappingDto.builder()
                        .valueMappingPerKey(Map.of("one", ValueMappingDto.builder().build()))
                        .build()
                ))
                .elementCollectionMappingPerKey(Map.of(
                        "five", ElementCollectionMappingDto.builder().build(),
                        "seven", ElementCollectionMappingDto.builder().build()
                ))
                .build()

        when:
        List<String> duplicateKeys = uniqueChildrenKeysValidator.findDuplicateKeys(elementMappingDto)

        then:
        duplicateKeys.size() == 2
        duplicateKeys.containsAll("two", "five")
    }

}
