package no.fintlabs.integration.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fintlabs.integration.model.configuration.entities.CollectionFieldConfiguration;
import no.fintlabs.integration.validation.constraints.ValueParsableAsType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@ValueParsableAsType
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CollectionFieldConfigurationDto {

    @NotBlank
    private String key;

    @NotNull
    private CollectionFieldConfiguration.Type type;

    Collection<String> values;

}
