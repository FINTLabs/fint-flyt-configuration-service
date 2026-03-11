package no.novari.flyt.configuration.model.configuration.dtos

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import no.novari.flyt.configuration.validation.constraints.IntegrationAndMetadataMatches
import java.time.Instant

@IntegrationAndMetadataMatches
data class ConfigurationDto(
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var id: Long? = null,
    @field:NotNull
    var integrationId: Long? = null,
    @field:NotNull
    var integrationMetadataId: Long? = null,
    var isCompleted: Boolean = false,
    var comment: String? = null,
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var version: Int? = null,
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:Valid
    @field:NotNull
    var mapping: ObjectMappingDto? = null,
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var lastModifiedAt: Instant? = null,
    @field:JsonProperty(access = JsonProperty.Access.READ_ONLY)
    var lastModifiedBy: String? = null,
) {
    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    fun toBuilder(): Builder =
        Builder()
            .id(id)
            .integrationId(integrationId)
            .integrationMetadataId(integrationMetadataId)
            .completed(isCompleted)
            .comment(comment)
            .version(version)
            .mapping(mapping)
            .lastModifiedAt(lastModifiedAt)
            .lastModifiedBy(lastModifiedBy)

    class Builder {
        private var id: Long? = null
        private var integrationId: Long? = null
        private var integrationMetadataId: Long? = null
        private var completed: Boolean = false
        private var comment: String? = null
        private var version: Int? = null
        private var mapping: ObjectMappingDto? = null
        private var lastModifiedAt: Instant? = null
        private var lastModifiedBy: String? = null

        fun id(id: Long?) = apply { this.id = id }

        fun integrationId(integrationId: Long?) = apply { this.integrationId = integrationId }

        fun integrationMetadataId(integrationMetadataId: Long?) =
            apply { this.integrationMetadataId = integrationMetadataId }

        fun completed(completed: Boolean) = apply { this.completed = completed }

        fun comment(comment: String?) = apply { this.comment = comment }

        fun version(version: Int?) = apply { this.version = version }

        fun mapping(mapping: ObjectMappingDto?) = apply { this.mapping = mapping }

        fun lastModifiedAt(lastModifiedAt: Instant?) = apply { this.lastModifiedAt = lastModifiedAt }

        fun lastModifiedBy(lastModifiedBy: String?) = apply { this.lastModifiedBy = lastModifiedBy }

        fun build(): ConfigurationDto =
            ConfigurationDto(
                id = id,
                integrationId = integrationId,
                integrationMetadataId = integrationMetadataId,
                isCompleted = completed,
                comment = comment,
                version = version,
                mapping = mapping,
                lastModifiedAt = lastModifiedAt,
                lastModifiedBy = lastModifiedBy,
            )
    }
}
