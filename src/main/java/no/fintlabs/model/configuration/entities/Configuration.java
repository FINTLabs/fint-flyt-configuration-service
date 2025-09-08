package no.fintlabs.model.configuration.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "configuration",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_integration_id_version",
                        columnNames = {"integration_id", "version"}
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Configuration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(name = "integration_id", nullable = false)
    private Long integrationId;

    @NotNull
    @Column(name = "integration_metadata_id", nullable = false)
    private Long integrationMetadataId;

    @Column(name = "version")
    private Integer version;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    @Column(name = "comment")
    private String comment;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mapping_id", referencedColumnName = "id", nullable = false)
    @Valid
    @NotNull
    private ObjectMapping mapping;

    @LastModifiedDate
    @Column(name = "last_modified_at", nullable = false)
    private Instant lastModifiedAt;

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @PrePersist
    void prePersistDefaults() {
        if (lastModifiedAt == null) lastModifiedAt = Instant.now();
        if (lastModifiedBy == null || lastModifiedBy.isBlank()) lastModifiedBy = "system";
    }
}