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
public class ElementCollectionMappingDto {

    private Collection<@Valid @NotNull ElementMappingDto> elementMappings = new ArrayList<>();

    private Collection<@Valid @NotNull ElementsFromCollectionMappingDto> elementsFromCollectionMappings = new ArrayList<>();

}
