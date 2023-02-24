package no.fintlabs.model.configuration.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Jacksonized
public class ObjectsFromCollectionMappingDto {

    @NotEmpty
    @Builder.Default
    private final List<@NotBlank String> instanceCollectionReferencesOrdered = new ArrayList<>();

    @Valid
    @NotNull
    private final ObjectMappingDto objectMappingDto;

}
