package no.novari.flyt.configuration

import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import jakarta.validation.groups.Default
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationDto
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationPageResponse
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationPatchDto
import no.novari.flyt.configuration.security.TokenParsingUtils
import no.novari.flyt.configuration.validation.ConfigurationValidatorFactory
import no.novari.flyt.configuration.validation.ValidationErrorsFormattingService
import no.novari.flyt.configuration.validation.groups.Completed
import no.novari.flyt.webresourceserver.UrlPaths.INTERNAL_API
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("$INTERNAL_API/konfigurasjoner")
class ConfigurationController(
    private val configurationService: ConfigurationService,
    private val configurationValidatorFactory: ConfigurationValidatorFactory,
    private val validationErrorsFormattingService: ValidationErrorsFormattingService,
    private val tokenParsingUtils: TokenParsingUtils,
) {
    @GetMapping
    fun getConfigurations(
        @RequestParam(name = "side") page: Int,
        @RequestParam(name = "antall") size: Int,
        @RequestParam(name = "sorteringFelt") sortProperty: String,
        @RequestParam(name = "sorteringRetning") sortDirection: Sort.Direction,
        @RequestParam(name = "integrasjonId", required = false) integrationId: Long?,
        @RequestParam(name = "ferdigstilt", required = false) complete: Boolean?,
        @RequestParam(name = "ekskluderMapping", required = false, defaultValue = "false") excludeMapping: Boolean,
    ): ConfigurationPageResponse {
        val filter = ConfigurationFilter(integrationId = integrationId, completed = complete)
        val pageRequest = PageRequest.of(page, size).withSort(sortDirection, sortProperty)

        val configurations = configurationService.findAll(filter, excludeMapping, pageRequest)

        return ConfigurationPageResponse(
            content = configurations.content,
            totalElements = configurations.totalElements,
            totalPages = configurations.totalPages,
        )
    }

    @GetMapping("{configurationId}")
    fun getConfiguration(
        @PathVariable configurationId: Long,
        @RequestParam(name = "ekskluderMapping", required = false, defaultValue = "false") excludeMapping: Boolean,
    ): ConfigurationDto {
        return configurationService.findById(configurationId, excludeMapping)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    @PostMapping
    fun postConfiguration(
        authentication: Authentication,
        @RequestBody configurationDto: ConfigurationDto,
    ): ConfigurationDto {
        tokenParsingUtils.bindAuditor(authentication).use {
            validateBeanConstraints(
                requireNotNull(configurationDto.integrationId),
                requireNotNull(configurationDto.integrationMetadataId),
                configurationDto,
            )
            return configurationService.save(configurationDto)
        }
    }

    @PatchMapping("{configurationId}")
    fun patchConfiguration(
        @PathVariable configurationId: Long,
        authentication: Authentication,
        @RequestBody configurationPatchDto: ConfigurationPatchDto,
    ): ConfigurationDto {
        tokenParsingUtils.bindAuditor(authentication).use {
            val configurationDto =
                configurationService.findById(configurationId, false)
                    ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

            validateIsNotCompleted(configurationDto)

            val configurationDtoBuilder =
                configurationDto.toBuilder().apply {
                    configurationPatchDto.integrationMetadataId?.let(this::integrationMetadataId)
                    configurationPatchDto.completed?.takeIf { it }?.let(this::completed)
                    configurationPatchDto.comment?.let(this::comment)
                    configurationPatchDto.mapping?.let(this::mapping)
                }

            val newConfigurationDto = configurationDtoBuilder.build()

            validateBeanConstraints(
                requireNotNull(newConfigurationDto.integrationId),
                requireNotNull(newConfigurationDto.integrationMetadataId),
                newConfigurationDto,
            )

            return configurationService.updateById(configurationId, configurationPatchDto)
        }
    }

    @DeleteMapping("{configurationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteConfiguration(
        @PathVariable configurationId: Long,
    ) {
        val configurationDto =
            configurationService.findById(configurationId, true)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

        validateIsNotCompleted(configurationDto)
        configurationService.deleteById(configurationId)
    }

    private fun validateIsNotCompleted(configurationDto: ConfigurationDto) {
        if (configurationDto.completed) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Configuration is completed, and cannot be altered",
            )
        }
    }

    private fun validateBeanConstraints(
        integrationId: Long,
        metadataId: Long,
        configurationDto: ConfigurationDto,
    ) {
        val validator: Validator = configurationValidatorFactory.getValidator(integrationId, metadataId)
        val constraintViolations: Set<ConstraintViolation<ConfigurationDto>> =
            if (configurationDto.completed) {
                validator.validate(configurationDto, Default::class.java, Completed::class.java)
            } else {
                validator.validate(configurationDto, Default::class.java)
            }

        if (constraintViolations.isNotEmpty()) {
            throw ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                validationErrorsFormattingService.format(constraintViolations),
            )
        }
    }
}
