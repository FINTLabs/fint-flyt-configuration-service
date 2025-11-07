package no.novari.configuration.model.configuration.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import no.novari.configuration.model.configuration.entities.ValueMapping;
import no.novari.configuration.validation.constraints.InstanceValueKeysAreDefinedInMetadata;
import no.novari.configuration.validation.constraints.InstanceValueTypesAreCompatible;
import no.novari.configuration.validation.constraints.ValueParsableAsType;
import no.novari.configuration.validation.groups.InstanceValueKeys;
import no.novari.configuration.validation.groups.InstanceValueTypes;
import no.novari.configuration.validation.groups.ValueParsability;

import jakarta.validation.constraints.NotNull;

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

}
