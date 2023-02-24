package no.fintlabs.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fintlabs.validation.constraints.AtLeastOneChild;
import no.fintlabs.validation.constraints.UniqueChildrenKeys;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@AtLeastOneChild
@UniqueChildrenKeys
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObjectMappingDto {

    private Map<String, @Valid @NotNull ValueMappingDto> valueMappingPerKey = new HashMap<>();

    private Map<String, @Valid @NotNull ObjectMappingDto> objectMappingPerKey = new HashMap<>();

    private Map<String, @Valid @NotNull ObjectCollectionMappingDto> objectCollectionMappingPerKey = new HashMap<>();

}
