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
public class ApplicantConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String organisationNumber;
    private String nationalIdentityNumber;

    @OneToMany(mappedBy = "applicantConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApplicantConfigurationField> fields = new LinkedHashSet<>();

    @OneToOne(mappedBy = "applicantConfiguration")
    private IntegrationConfiguration integrationConfiguration;
}