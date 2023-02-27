package no.fintlabs.model.configuration.dtos.object;

import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
@Jacksonized
public class ObjectsFromCollectionMappingDto {

    @NotEmpty
    @Builder.Default
    private final List<@NotBlank String> instanceCollectionReferencesOrdered = new ArrayList<>();

    @Valid
    @NotNull
    private final ObjectMappingDto objectMapping;

}
