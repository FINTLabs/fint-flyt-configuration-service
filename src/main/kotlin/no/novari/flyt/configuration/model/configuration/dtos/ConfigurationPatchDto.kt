package no.novari.flyt.configuration.model.configuration.dtos

data class ConfigurationPatchDto(
    val integrationMetadataId: Long? = null,
    val completed: Boolean? = null,
    val comment: String? = null,
    val mapping: ObjectMappingDto? = null,
)
