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
@Table
public class ApplicantConfigurationField extends Field {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "applicant_configuration_id")
    @JsonIgnore
    private ApplicantConfiguration applicantConfiguration;

}