package no.novari.flyt.configuration

import no.novari.flyt.configuration.mapping.ConfigurationMappingService
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationDto
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationPatchDto
import no.novari.flyt.configuration.model.configuration.entities.Configuration
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ConfigurationService(
    private val configurationRepository: ConfigurationRepository,
    private val configurationMappingService: ConfigurationMappingService,
    private val objectMappingRepository: ObjectMappingRepository,
) {
    fun findById(
        configurationId: Long,
        excludeMapping: Boolean,
    ): ConfigurationDto? =
        configurationRepository.findByIdOrNull(configurationId)?.let {
            configurationMappingService.toDto(it, excludeMapping)
        }

    fun findAll(
        filter: ConfigurationFilter,
        excludeMapping: Boolean,
        pageable: Pageable,
    ): Page<ConfigurationDto> {
        val configurationExample =
            Configuration().apply {
                filter.integrationId?.let { integrationId = it }
                filter.completed?.let { isCompleted = it }
            }

        return configurationRepository
            .findAll(Example.of(configurationExample), pageable)
            .map { configuration ->
                configurationMappingService.toDto(configuration, excludeMapping)
            }
    }

    fun save(configurationDto: ConfigurationDto): ConfigurationDto =
        configurationMappingService.toDto(
            configurationRepository.saveWithVersion(configurationMappingService.toEntity(configurationDto)),
            false,
        )

    fun updateById(
        configurationId: Long,
        configurationPatchDto: ConfigurationPatchDto,
    ): ConfigurationDto {
        val configuration = requireNotNull(configurationRepository.findByIdOrNull(configurationId))

        configurationPatchDto.integrationMetadataId?.let { configuration.integrationMetadataId = it }
        configurationPatchDto.completed?.takeIf { it }?.let { configuration.isCompleted = it }
        configurationPatchDto.comment?.let { configuration.comment = it }
        configurationPatchDto.mapping
            ?.let(configurationMappingService::toEntity)
            ?.let(objectMappingRepository::save)
            ?.let { configuration.mapping = it }

        return configurationMappingService.toDto(
            configurationRepository.saveWithVersion(configuration),
            false,
        )
    }

    fun deleteById(configurationId: Long) = configurationRepository.deleteById(configurationId)
}
