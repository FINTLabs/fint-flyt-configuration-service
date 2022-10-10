package no.fintlabs.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import no.fintlabs.integration.validation.constraints.UniqueChildrenKeys;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    private long id;

    @NotNull
    private Long integrationId;

    @NotNull
    private Long integrationMetadataId;

    private Integer version;

    private boolean completed;

    private String comment;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "configuration_id")
    @UniqueChildrenKeys
    private Collection<@Valid ConfigurationElement> elements = new ArrayList<>();

}
