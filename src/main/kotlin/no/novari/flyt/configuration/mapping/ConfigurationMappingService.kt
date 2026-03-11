package no.novari.flyt.configuration.mapping

import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationDto
import no.novari.flyt.configuration.model.configuration.dtos.ObjectMappingDto
import no.novari.flyt.configuration.model.configuration.entities.Configuration
import no.novari.flyt.configuration.model.configuration.entities.ObjectMapping
import org.springframework.stereotype.Service

@Service
class ConfigurationMappingService(
    private val objectMappingMappingService: ObjectMappingMappingService,
) {
    fun toEntity(configurationDto: ConfigurationDto): Configuration =
        Configuration
            .builder()
            .integrationId(configurationDto.integrationId)
            .integrationMetadataId(configurationDto.integrationMetadataId)
            .comment(configurationDto.comment)
            .completed(configurationDto.isCompleted)
            .mapping(toEntity(requireNotNull(configurationDto.mapping)))
            .build()

    fun toDto(
        configuration: Configuration,
        excludeMapping: Boolean,
    ): ConfigurationDto =
        ConfigurationDto
            .builder()
            .id(configuration.id)
            .integrationId(configuration.integrationId)
            .integrationMetadataId(configuration.integrationMetadataId)
            .version(configuration.version)
            .completed(configuration.isCompleted)
            .comment(configuration.comment)
            .mapping(configuration.mapping?.takeUnless { excludeMapping }?.let(::toDto))
            .lastModifiedBy(configuration.lastModifiedBy)
            .lastModifiedAt(configuration.lastModifiedAt)
            .build()

    fun toEntity(objectMappingDto: ObjectMappingDto): ObjectMapping =
        objectMappingMappingService.toEntity(objectMappingDto)

    fun toDto(objectMapping: ObjectMapping): ObjectMappingDto = objectMappingMappingService.toDto(objectMapping)
}
