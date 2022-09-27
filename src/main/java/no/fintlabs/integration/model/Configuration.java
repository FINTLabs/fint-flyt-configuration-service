package no.fintlabs.integration.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited(withModifiedFlag = true)
@Entity
public class Configuration {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Setter(AccessLevel.NONE)
    private UUID id;

    @NotNull
    private String integrationId;

    @NotNull
    private String integrationMetadataId; // what if this is updated since last edit on uncompleted configuration?

    private Integer version; // Only set when completed

    private boolean completed;

    private String comment;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "configuration_id")
    private Collection<ConfigurationElement> elements = new ArrayList<>();

}
