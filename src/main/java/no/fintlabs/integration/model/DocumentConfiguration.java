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
@Table(name = "document_configuration")
public class DocumentConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @OneToMany(mappedBy = "documentConfiguration", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DocumentConfigurationField> fields = new LinkedHashSet<>();

    @OneToOne(mappedBy = "documentConfiguration", cascade = CascadeType.ALL)
    @JsonIgnore
    private IntegrationConfiguration integrationConfiguration;
}
