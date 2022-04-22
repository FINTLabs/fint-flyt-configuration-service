package no.fintlabs.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "application_configuration")
public class ApplicantConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    private String organisationNumber;
    private String nationalIdentityNumber;

    @OneToMany(mappedBy = "applicantConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApplicantConfigurationField> fields = new LinkedHashSet<>();

    @OneToOne(mappedBy = "applicantConfiguration", cascade = CascadeType.ALL)
    @JsonIgnore
    private IntegrationConfiguration integrationConfiguration;
}