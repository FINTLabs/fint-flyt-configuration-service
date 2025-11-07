package no.novari.flyt.configuration.model.configuration.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping;
import no.novari.flyt.configuration.validation.constraints.InstanceValueKeysAreDefinedInMetadata;
import no.novari.flyt.configuration.validation.constraints.InstanceValueTypesAreCompatible;
import no.novari.flyt.configuration.validation.constraints.ValueParsableAsType;
import no.novari.flyt.configuration.validation.groups.InstanceValueKeys;
import no.novari.flyt.configuration.validation.groups.InstanceValueTypes;
import no.novari.flyt.configuration.validation.groups.ValueParsability;

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
