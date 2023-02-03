package no.fintlabs.model.configuration.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UniqueIntegrationIdAndVersion", columnNames = {"integrationId", "version"})
})
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    private Long integrationId;

    @NotNull
    private Long integrationMetadataId;

    private Integer version;

    private boolean completed;

    private String comment;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mapping_id", referencedColumnName = "id")
    @Valid
    @NotNull
    ElementMapping mapping;

}
