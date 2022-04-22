package no.fintlabs.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "case_configuration_field")
public class CaseConfigurationField extends Field {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "case_configuration_id")
    @JsonIgnore
    private CaseConfiguration caseConfiguration;
}