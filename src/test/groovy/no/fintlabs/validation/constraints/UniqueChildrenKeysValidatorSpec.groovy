package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.dtos.object.ObjectCollectionMappingDto
import no.fintlabs.model.configuration.dtos.object.ObjectMappingDto
import no.fintlabs.model.configuration.dtos.value.ValueCollectionMappingDto
import no.fintlabs.model.configuration.dtos.value.ValueMappingDto
import spock.lang.Specification

class UniqueChildrenKeysValidatorSpec extends Specification {

    UniqueChildrenKeysValidator uniqueChildrenKeysValidator

    def setup() {
        uniqueChildrenKeysValidator = new UniqueChildrenKeysValidator()
    }

    def 'should return duplicate keys in a single collection and across child collections'() {
        given:
        ObjectMappingDto objectMappingDto = ObjectMappingDto
                .builder()
                .valueMappingPerKey(Map.of(
                        "one", ValueMappingDto.builder().build(),
                        "two", ValueMappingDto.builder().build(),
                        "six", ValueMappingDto.builder().build(),
                ))
                .valueCollectionMappingPerKey(Map.of(
                        "one", ValueCollectionMappingDto.builder().build(),
                        "eight", ValueCollectionMappingDto.builder().build()
                ))
                .objectMappingPerKey(Map.of(
                        "two", ObjectMappingDto.builder().build(),
                        "three", ObjectMappingDto.builder().build(),
                        "five", ObjectMappingDto.builder().build(),
                        "four", ObjectMappingDto.builder()
                        .valueMappingPerKey(Map.of("three", ValueMappingDto.builder().build()))
                        .build()
                ))
                .objectCollectionMappingPerKey(Map.of(
                        "five", ObjectCollectionMappingDto.builder().build(),
                        "seven", ObjectCollectionMappingDto.builder().build()
                ))
                .build()

        when:
        List<String> duplicateKeys = uniqueChildrenKeysValidator.findDuplicateKeys(objectMappingDto)

        then:
        duplicateKeys.size() == 3
        duplicateKeys.containsAll("one", "two", "five")
    }

}
