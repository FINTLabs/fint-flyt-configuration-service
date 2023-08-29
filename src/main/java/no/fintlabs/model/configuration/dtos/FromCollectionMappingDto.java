package no.fintlabs.model.configuration.dtos;

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
import java.util.Optional;

@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class FromCollectionMappingDto<T> {

    public FromCollectionMappingDto(List<String> instanceCollectionReferencesOrdered, T elementMapping) {
        this.instanceCollectionReferencesOrdered = Optional.ofNullable(instanceCollectionReferencesOrdered).orElse(new ArrayList<>());
        this.elementMapping = elementMapping;
    }

    @NotEmpty
    private final List<@NotBlank String> instanceCollectionReferencesOrdered;

    @Valid
    @NotNull
    private final T elementMapping;

    @Override
    public String toString() {
        return "Sensitive data omitted";
    }

}
