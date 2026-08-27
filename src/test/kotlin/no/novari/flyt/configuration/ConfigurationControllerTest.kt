package no.novari.flyt.configuration

import jakarta.validation.Validator
import jakarta.validation.groups.Default
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationDto
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationPatchDto
import no.novari.flyt.configuration.validation.ConfigurationValidatorFactory
import no.novari.flyt.configuration.validation.ValidationErrorsFormattingService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class ConfigurationControllerTest {
    private lateinit var configurationService: ConfigurationService
    private lateinit var configurationValidatorFactory: ConfigurationValidatorFactory
    private lateinit var validationErrorsFormattingService: ValidationErrorsFormattingService
    private lateinit var validator: Validator

    private lateinit var configurationController: ConfigurationController

    @BeforeEach
    fun setUp() {
        configurationService = mock()
        configurationValidatorFactory = mock()
        validationErrorsFormattingService = mock()
        validator = mock()

        configurationController =
            ConfigurationController(
                configurationService,
                configurationValidatorFactory,
                validationErrorsFormattingService,
            )
    }

    @Test
    fun `getConfigurations returns page content with totals`() {
        val configuration =
            ConfigurationDto
                .builder()
                .id(123L)
                .integrationId(1L)
                .integrationMetadataId(2L)
                .build()
        val page = PageImpl(listOf(configuration), PageRequest.of(0, 10), 1)

        whenever(configurationService.findAll(any(), eq(false), any())).thenReturn(page)

        val response =
            configurationController.getConfigurations(
                page = 0,
                size = 10,
                sortProperty = "id",
                sortDirection = Sort.Direction.ASC,
                integrationId = null,
                complete = null,
                excludeMapping = false,
            )

        assertEquals(listOf(configuration), response.content)
        assertEquals(1L, response.totalElements)
        assertEquals(1, response.totalPages)
    }

    @Test
    fun `postConfiguration returns unprocessable entity when integration metadata id is missing`() {
        val configuration =
            ConfigurationDto
                .builder()
                .integrationId(5L)
                .build()

        val exception =
            assertThrows(ResponseStatusException::class.java) {
                configurationController.postConfiguration(configuration)
            }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.statusCode)
        assertEquals("integrationMetadataId must not be null", exception.reason)
        verifyNoInteractions(configurationService, configurationValidatorFactory)
    }

    @Test
    fun `patchConfiguration validates and updates the configuration`() {
        val existingConfiguration =
            ConfigurationDto
                .builder()
                .id(123L)
                .integrationId(1L)
                .integrationMetadataId(2L)
                .build()
        val patchDto = ConfigurationPatchDto(comment = "Updated comment")

        whenever(configurationService.findById(123L, false)).thenReturn(existingConfiguration)
        whenever(configurationValidatorFactory.getValidator(1L, 2L)).thenReturn(validator)
        whenever(validator.validate(any<ConfigurationDto>(), eq(Default::class.java))).thenReturn(emptySet())
        whenever(
            configurationService.updateById(123L, patchDto),
        ).thenReturn(existingConfiguration.copy(comment = patchDto.comment))

        configurationController.patchConfiguration(123L, patchDto)

        verify(configurationService).updateById(123L, patchDto)
    }
}
