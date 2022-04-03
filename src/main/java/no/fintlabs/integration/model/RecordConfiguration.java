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
@Table
public class RecordConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @OneToMany(mappedBy = "recordConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecordConfigurationField> fields = new LinkedHashSet<>();

    @OneToOne(mappedBy = "recordConfiguration", cascade = CascadeType.ALL)
    @JsonIgnore
    private IntegrationConfiguration integrationConfiguration;
}
