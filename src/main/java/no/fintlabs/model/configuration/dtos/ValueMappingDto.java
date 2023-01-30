package no.fintlabs.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.validation.constraints.InstanceFieldReferenceKeysExistInMetadata;
import no.fintlabs.validation.constraints.InstanceFieldReferenceValueTypesAreCompatible;
import no.fintlabs.validation.constraints.ValueParsableAsType;
import no.fintlabs.validation.groups.MetadataKeysValidationGroup;
import no.fintlabs.validation.groups.MetadataTypeValidationGroup;

import javax.validation.GroupSequence;

@ValueParsableAsType
@InstanceFieldReferenceKeysExistInMetadata(groups = MetadataKeysValidationGroup.class)
@InstanceFieldReferenceValueTypesAreCompatible(groups = MetadataTypeValidationGroup.class)
@GroupSequence({ValueMappingDto.class, MetadataKeysValidationGroup.class, MetadataTypeValidationGroup.class})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValueMappingDto {

    private ValueMapping.Type type;
    private String mappingString;
}
