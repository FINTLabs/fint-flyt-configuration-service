package no.novari.flyt.configuration.validation.constraints;

import no.novari.flyt.configuration.model.configuration.dtos.CollectionMappingDto;
import no.novari.flyt.configuration.model.configuration.dtos.ObjectMappingDto;
import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto;
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UniqueChildrenKeysValidatorTest {

    private UniqueChildrenKeysValidator uniqueChildrenKeysValidator;

    @BeforeEach
    void setup() {
        uniqueChildrenKeysValidator = new UniqueChildrenKeysValidator();
    }

    @Test
    public void shouldReturnTrueWhenNoDuplicateKeysExist() {
        ObjectMappingDto objectMappingDto = ObjectMappingDto.builder()
                .valueMappingPerKey(Map.of(
                        "one", ValueMappingDto.builder().mappingString("stringOne").type(ValueMapping.Type.STRING).build(),
                        "two", ValueMappingDto.builder().mappingString("stringTwo").type(ValueMapping.Type.URL).build()
                ))
                .build();

        boolean isValid = uniqueChildrenKeysValidator.isValid(objectMappingDto, null);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnDuplicateKeysInASingleCollectionAndAcrossChildCollections() {
        Map<String, ValueMappingDto> valueMappingPerKey = new HashMap<>();
        valueMappingPerKey.put("one", ValueMappingDto.builder().build());
        valueMappingPerKey.put("two", ValueMappingDto.builder().build());
        valueMappingPerKey.put("six", ValueMappingDto.builder().build());

        Map<String, CollectionMappingDto<ValueMappingDto>> valueCollectionMappingPerKey = new HashMap<>();
        valueCollectionMappingPerKey.put("one", CollectionMappingDto.<ValueMappingDto>builder().build());
        valueCollectionMappingPerKey.put("eight", CollectionMappingDto.<ValueMappingDto>builder().build());

        Map<String, ObjectMappingDto> objectMappingPerKey = new HashMap<>();
        objectMappingPerKey.put("two", ObjectMappingDto.builder().build());
        objectMappingPerKey.put("three", ObjectMappingDto.builder().build());
        objectMappingPerKey.put("five", ObjectMappingDto.builder().build());
        objectMappingPerKey.put("four", ObjectMappingDto.builder().valueMappingPerKey(Map.of("three", ValueMappingDto.builder().build())).build());

        Map<String, CollectionMappingDto<ObjectMappingDto>> objectCollectionMappingPerKey = new HashMap<>();
        objectCollectionMappingPerKey.put("five", CollectionMappingDto.<ObjectMappingDto>builder().build());
        objectCollectionMappingPerKey.put("seven", CollectionMappingDto.<ObjectMappingDto>builder().build());

        ObjectMappingDto objectMappingDto = ObjectMappingDto.builder()
                .valueMappingPerKey(valueMappingPerKey)
                .valueCollectionMappingPerKey(valueCollectionMappingPerKey)
                .objectMappingPerKey(objectMappingPerKey)
                .objectCollectionMappingPerKey(objectCollectionMappingPerKey)
                .build();

        List<String> duplicateKeys = uniqueChildrenKeysValidator.findDuplicateKeys(objectMappingDto);

        assertEquals(3, duplicateKeys.size());
        assertTrue(duplicateKeys.containsAll(List.of("one", "two", "five")));
    }
}
