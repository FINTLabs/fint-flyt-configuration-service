package no.novari.flyt.configuration.model.configuration.dtos

data class ConfigurationPageResponse(
    val content: List<ConfigurationDto>,
    val totalElements: Long,
    val totalPages: Int,
)
