package no.novari.flyt.configuration.model.configuration.entities

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(
    name = "configuration",
    uniqueConstraints = [
        UniqueConstraint(
            name = "unique_integration_id_version",
            columnNames = ["integration_id", "version"],
        ),
    ],
)
@EntityListeners(AuditingEntityListener::class)
class Configuration(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var id: Long? = null,
    @field:NotNull
    @field:Column(name = "integration_id", nullable = false)
    var integrationId: Long? = null,
    @field:NotNull
    @field:Column(name = "integration_metadata_id", nullable = false)
    var integrationMetadataId: Long? = null,
    @field:Column(name = "version")
    var version: Int? = null,
    @field:Column(name = "completed", nullable = false)
    var completed: Boolean = false,
    @field:Column(name = "comment", columnDefinition = "text")
    var comment: String? = null,
    @field:OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @field:JoinColumn(name = "mapping_id", referencedColumnName = "id", nullable = false)
    @field:Valid
    @field:NotNull
    var mapping: ObjectMapping? = null,
    @field:LastModifiedDate
    @field:Column(name = "last_modified_at", nullable = false)
    var lastModifiedAt: Instant? = null,
    @field:LastModifiedBy
    @field:Column(name = "last_modified_by", nullable = false)
    var lastModifiedBy: String? = null,
) {
    @PrePersist
    fun prePersistDefaults() {
        if (lastModifiedAt == null) {
            lastModifiedAt = Instant.now()
        }
        if (lastModifiedBy.isNullOrBlank()) {
            lastModifiedBy = "system"
        }
    }

    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var id: Long? = null
        private var integrationId: Long? = null
        private var integrationMetadataId: Long? = null
        private var version: Int? = null
        private var completed: Boolean = false
        private var comment: String? = null
        private var mapping: ObjectMapping? = null
        private var lastModifiedAt: Instant? = null
        private var lastModifiedBy: String? = null

        fun id(id: Long?) = apply { this.id = id }

        fun integrationId(integrationId: Long?) = apply { this.integrationId = integrationId }

        fun integrationMetadataId(integrationMetadataId: Long?) =
            apply {
                this.integrationMetadataId =
                    integrationMetadataId
            }

        fun version(version: Int?) = apply { this.version = version }

        fun completed(completed: Boolean) = apply { this.completed = completed }

        fun comment(comment: String?) = apply { this.comment = comment }

        fun mapping(mapping: ObjectMapping?) = apply { this.mapping = mapping }

        fun lastModifiedAt(lastModifiedAt: Instant?) = apply { this.lastModifiedAt = lastModifiedAt }

        fun lastModifiedBy(lastModifiedBy: String?) = apply { this.lastModifiedBy = lastModifiedBy }

        fun build(): Configuration =
            Configuration(
                id = id,
                integrationId = integrationId,
                integrationMetadataId = integrationMetadataId,
                version = version,
                completed = completed,
                comment = comment,
                mapping = mapping,
                lastModifiedAt = lastModifiedAt,
                lastModifiedBy = lastModifiedBy,
            )
    }
}
