package no.novari.flyt.configuration

import jakarta.validation.Validator
import jakarta.validation.groups.Default
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationDto
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationPatchDto
import no.novari.flyt.configuration.security.AuditorScope
import no.novari.flyt.configuration.security.TokenParsingUtils
import no.novari.flyt.configuration.validation.ConfigurationValidatorFactory
import no.novari.flyt.configuration.validation.ValidationErrorsFormattingService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.core.Authentication

class ConfigurationControllerTest {
    private lateinit var configurationService: ConfigurationService
    private lateinit var configurationValidatorFactory: ConfigurationValidatorFactory
    private lateinit var validationErrorsFormattingService: ValidationErrorsFormattingService
    private lateinit var tokenParsingUtils: TokenParsingUtils
    private lateinit var validator: Validator

    private lateinit var configurationController: ConfigurationController

    @BeforeEach
    fun setUp() {
        configurationService = mock()
        configurationValidatorFactory = mock()
        validationErrorsFormattingService = mock()
        tokenParsingUtils = mock()
        validator = mock()

        configurationController =
            ConfigurationController(
                configurationService,
                configurationValidatorFactory,
                validationErrorsFormattingService,
                tokenParsingUtils,
            )
    }

    @Test
    fun patchConfigurationShouldBindAuditorBeforeUpdating() {
        val authentication = mock<Authentication>()
        val existingConfiguration =
            ConfigurationDto
                .builder()
                .id(123L)
                .integrationId(1L)
                .integrationMetadataId(2L)
                .build()
        val patchDto = ConfigurationPatchDto(comment = "Updated comment")

        whenever(tokenParsingUtils.bindAuditor(authentication)).thenReturn(AuditorScope {})
        whenever(configurationService.findById(123L, false)).thenReturn(existingConfiguration)
        whenever(configurationValidatorFactory.getValidator(1L, 2L)).thenReturn(validator)
        whenever(validator.validate(any<ConfigurationDto>(), eq(Default::class.java))).thenReturn(emptySet())
        whenever(configurationService.updateById(123L, patchDto)).thenReturn(existingConfiguration.copy(comment = patchDto.comment))

        configurationController.patchConfiguration(123L, authentication, patchDto)

        verify(tokenParsingUtils).bindAuditor(authentication)
        verify(configurationService).updateById(123L, patchDto)
    }
}
