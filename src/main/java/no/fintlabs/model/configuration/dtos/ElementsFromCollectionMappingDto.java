package no.fintlabs.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElementsFromCollectionMappingDto {

    @Builder.Default
    private List<String> instanceCollectionReferencesOrdered = new ArrayList<>();
    private ElementMappingDto elementMapping;
}
