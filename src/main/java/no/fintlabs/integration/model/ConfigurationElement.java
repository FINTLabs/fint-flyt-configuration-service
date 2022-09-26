package no.fintlabs.integration.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited(withModifiedFlag = true)
@Entity
public class ConfigurationElement {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Setter(AccessLevel.NONE)
    private UUID id;

    private String key; // TODO: 20/09/2022 Unique combined with configurationId

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_configuration_element_id")
    private Collection<ConfigurationElement> elements;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "configuration_element_id")
    private Collection<FieldConfiguration> fieldConfigurations;

}
