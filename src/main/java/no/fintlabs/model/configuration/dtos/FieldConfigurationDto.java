package no.fintlabs.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.fintlabs.model.configuration.entities.FieldConfiguration;
import no.fintlabs.validation.constraints.InstanceFieldReferenceKeysExistInMetadata;
import no.fintlabs.validation.constraints.InstanceFieldReferenceValueTypesAreCompatible;
import no.fintlabs.validation.constraints.ValueParsableAsType;
import no.fintlabs.validation.groups.MetadataKeysValidationGroup;
import no.fintlabs.validation.groups.MetadataTypeValidationGroup;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ValueParsableAsType
@InstanceFieldReferenceKeysExistInMetadata(groups = MetadataKeysValidationGroup.class)
@InstanceFieldReferenceValueTypesAreCompatible(groups = MetadataTypeValidationGroup.class)
@GroupSequence({FieldConfigurationDto.class, MetadataKeysValidationGroup.class, MetadataTypeValidationGroup.class})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldConfigurationDto {

    @NotBlank
    private String key;

    @NotNull
    private FieldConfiguration.Type type;

    private String value;

}
