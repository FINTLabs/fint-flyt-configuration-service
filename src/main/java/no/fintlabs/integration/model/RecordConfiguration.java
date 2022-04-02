package no.fintlabs.integration.model;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class RecordConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "recordConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecordConfigurationField> fields = new LinkedHashSet<>();

    @OneToOne(mappedBy = "recordConfiguration")
    private IntegrationConfiguration integrationConfiguration;
}
