package no.fintlabs.integration.model.configuration.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fintlabs.integration.validation.constraints.IntegrationAndMetadataMatches;
import no.fintlabs.integration.validation.constraints.UniqueChildrenKeys;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IntegrationAndMetadataMatches
public class ConfigurationDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    private Long integrationId;

    @NotNull
    private Long integrationMetadataId;

    private boolean completed;

    private String comment;

    @JsonInclude(Include.NON_NULL)
    @UniqueChildrenKeys
    private Collection<@Valid @NotNull ConfigurationElementDto> elements;

}
