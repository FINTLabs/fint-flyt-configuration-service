package no.fintlabs.integration.model.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import no.fintlabs.integration.validation.constraints.InstanceFieldReferenceKeysExistInMetadata;
import no.fintlabs.integration.validation.constraints.InstanceFieldReferenceValueTypesAreCompatible;
import no.fintlabs.integration.validation.constraints.ValueParsableAsType;
import no.fintlabs.integration.validation.groups.MetadataKeysValidationGroup;
import no.fintlabs.integration.validation.groups.MetadataTypeValidationGroup;

import javax.persistence.*;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValueParsableAsType
@InstanceFieldReferenceKeysExistInMetadata(groups = MetadataKeysValidationGroup.class)
@InstanceFieldReferenceValueTypesAreCompatible(groups = MetadataTypeValidationGroup.class)
@GroupSequence({FieldConfiguration.class, MetadataKeysValidationGroup.class, MetadataTypeValidationGroup.class})
@Entity
public class FieldConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private long id;

    public enum Type {
        STRING, URL, BOOLEAN, DYNAMIC_STRING
    }

    @NotBlank
    private String key;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Type type;

    private String value;

}
