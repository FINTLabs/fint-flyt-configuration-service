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
public class DocumentConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "documentConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentConfigurationField> fields = new LinkedHashSet<>();

    @OneToOne(mappedBy = "documentConfiguration")
    private IntegrationConfiguration integrationConfiguration;
}
