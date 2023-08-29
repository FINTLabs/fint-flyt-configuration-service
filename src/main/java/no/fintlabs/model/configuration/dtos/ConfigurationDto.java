package no.fintlabs.model.configuration.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import no.fintlabs.validation.constraints.IntegrationAndMetadataMatches;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
@Jacksonized
@IntegrationAndMetadataMatches
public class ConfigurationDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Long id;

    @NotNull
    private final Long integrationId;

    @NotNull
    private final Long integrationMetadataId;

    private final boolean completed;

    private final String comment;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Integer version;

    @JsonInclude(Include.NON_NULL)
    @Valid
    @NotNull
    private final ObjectMappingDto mapping;

    @Override
    public String toString() {
        return "Sensitive data omitted";
    }

}
