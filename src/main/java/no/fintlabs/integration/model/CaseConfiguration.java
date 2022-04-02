package no.fintlabs.integration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class CaseConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private CaseCreationStrategy caseCreationStrategy;
    private String caseNumber;

    @OneToMany(mappedBy = "caseConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CaseConfigurationField> fields = new LinkedHashSet<>();

    @OneToOne(mappedBy = "caseConfiguration")
    private IntegrationConfiguration integrationConfiguration;


}