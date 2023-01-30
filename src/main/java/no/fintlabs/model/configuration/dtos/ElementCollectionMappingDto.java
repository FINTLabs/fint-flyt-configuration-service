package no.fintlabs.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElementCollectionMappingDto {

    @Builder.Default
    private Collection<ElementMappingDto> elementMappings = new ArrayList<>();

    @Builder.Default
    private Collection<ElementsFromCollectionMappingDto> elementsFromCollectionMappings = new ArrayList<>();
}
