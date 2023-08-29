package no.fintlabs.model.configuration.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.validation.constraints.InstanceValueKeysAreDefinedInMetadata;
import no.fintlabs.validation.constraints.InstanceValueTypesAreCompatible;
import no.fintlabs.validation.constraints.ValueParsableAsType;
import no.fintlabs.validation.groups.InstanceValueKeys;
import no.fintlabs.validation.groups.InstanceValueTypes;
import no.fintlabs.validation.groups.ValueParsability;

import javax.validation.constraints.NotNull;

@ValueParsableAsType(groups = ValueParsability.class)
@InstanceValueKeysAreDefinedInMetadata(groups = InstanceValueKeys.class)
@InstanceValueTypesAreCompatible(groups = InstanceValueTypes.class)
@Getter
@Builder
@EqualsAndHashCode
@Jacksonized
public class ValueMappingDto {

    @NotNull
    private final ValueMapping.Type type;

    @NotNull
    private final String mappingString;

    @Override
    public String toString() {
        return "Sensitive data omitted";
    }

}
