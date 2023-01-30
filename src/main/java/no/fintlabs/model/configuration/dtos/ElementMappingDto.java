package no.fintlabs.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fintlabs.validation.constraints.UniqueChildrenKeys;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@UniqueChildrenKeys
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElementMappingDto {

    private Map<String, @Valid @NotNull ValueMappingDto> valueMappingPerKey = new HashMap<>();

    private Map<String, @Valid @NotNull ElementMappingDto> elementMappingPerKey = new HashMap<>();

    private Map<String, @Valid @NotNull ElementCollectionMappingDto> elementCollectionMappingPerKey = new HashMap<>();

}
