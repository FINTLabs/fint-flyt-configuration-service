package no.fintlabs.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.validation.constraints.InstanceFieldReferenceKeysExistInMetadata;
import no.fintlabs.validation.constraints.InstanceFieldReferenceValueTypesAreCompatible;
import no.fintlabs.validation.constraints.ValueParsableAsType;
import no.fintlabs.validation.groups.MetadataKeys;
import no.fintlabs.validation.groups.MetadataType;
import no.fintlabs.validation.groups.ValueParsability;

import javax.validation.constraints.NotNull;

@ValueParsableAsType(groups = ValueParsability.class)
@InstanceFieldReferenceKeysExistInMetadata(groups = MetadataKeys.class)
@InstanceFieldReferenceValueTypesAreCompatible(groups = MetadataType.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueMappingDto {

    @NotNull
    private ValueMapping.Type type;

    @NotNull
    private String mappingString;

}
