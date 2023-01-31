package no.fintlabs.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElementsFromCollectionMappingDto {

    @NotEmpty
    private List<@NotBlank String> instanceCollectionReferencesOrdered = new ArrayList<>();

    @NotNull
    private ElementMappingDto elementMapping;

}
