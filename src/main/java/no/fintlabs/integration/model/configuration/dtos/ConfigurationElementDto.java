package no.fintlabs.integration.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fintlabs.integration.validation.constraints.UniqueChildrenKeys;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@UniqueChildrenKeys
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationElementDto {

    @NotBlank
    private String key;

    private Collection<@Valid @NotNull ConfigurationElementDto> elements = new ArrayList<>();

    private Collection<@Valid @NotNull FieldConfigurationDto> fieldConfigurations = new ArrayList<>();

    private Collection<@Valid @NotNull CollectionFieldConfigurationDto> collectionFieldConfigurations = new ArrayList<>();

}
