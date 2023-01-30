package no.fintlabs.model.configuration.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fintlabs.validation.constraints.IntegrationAndMetadataMatches;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Data
@Builder(toBuilder = true)
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer version;

    @JsonInclude(Include.NON_NULL)
    @Valid
    @NotNull
    private ElementMappingDto mapping;

}
