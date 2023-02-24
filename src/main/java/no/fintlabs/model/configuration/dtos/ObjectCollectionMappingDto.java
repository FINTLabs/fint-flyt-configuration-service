package no.fintlabs.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObjectCollectionMappingDto {

    private Collection<@Valid @NotNull ObjectMappingDto> objectMappings = new ArrayList<>();

    private Collection<@Valid @NotNull ObjectsFromCollectionMappingDto> objectsFromCollectionMappings = new ArrayList<>();

}
