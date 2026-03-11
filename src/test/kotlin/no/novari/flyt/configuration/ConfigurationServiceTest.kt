package no.novari.flyt.configuration

import no.novari.flyt.configuration.mapping.ConfigurationMappingService
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationDto
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationPatchDto
import no.novari.flyt.configuration.model.configuration.entities.Configuration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.Optional

class ConfigurationServiceTest {
    private lateinit var configurationRepository: ConfigurationRepository
    private lateinit var configurationMappingService: ConfigurationMappingService
    private lateinit var objectMappingRepository: ObjectMappingRepository

    private lateinit var configurationService: ConfigurationService

    @BeforeEach
    fun setUp() {
        configurationRepository = mock()
        configurationMappingService = mock()
        objectMappingRepository = mock()
        configurationService =
            ConfigurationService(configurationRepository, configurationMappingService, objectMappingRepository)
    }

    @Test
    fun testFindById() {
        val configuration = Configuration().apply { integrationId = 123L }
        val configurationDto = ConfigurationDto.builder().integrationId(123L).build()

        whenever(configurationRepository.findById(any())).thenReturn(Optional.of(configuration))
        whenever(configurationMappingService.toDto(any(), any())).thenReturn(configurationDto)

        val result = configurationService.findById(123L, true)

        assertEquals(configurationDto, result)
    }

    @Test
    fun testFindAll() {
        val configuration = Configuration().apply { integrationId = 123L }
        val configurationDto = ConfigurationDto.builder().integrationId(123L).build()

        whenever(
            configurationRepository.findAll(
                any<org.springframework.data.domain.Example<Configuration>>(),
                any<Pageable>(),
            ),
        ).thenReturn(PageImpl(listOf(configuration)))

        whenever(configurationMappingService.toDto(any(), any())).thenReturn(configurationDto)

        val filter = ConfigurationFilter(123L, null)

        val result: Page<ConfigurationDto> = configurationService.findAll(filter, true, Pageable.unpaged())

        assertFalse(result.isEmpty)
        assertEquals(configurationDto, result.content[0])
    }

    @Test
    fun testSave() {
        val configurationDto = ConfigurationDto.builder().integrationId(123L).build()
        val configuration = Configuration.builder().integrationId(123L).build()

        whenever(configurationMappingService.toEntity(any<ConfigurationDto>())).thenReturn(configuration)
        whenever(configurationRepository.saveWithVersion(any<Configuration>())).thenReturn(configuration)
        whenever(configurationMappingService.toDto(any(), any())).thenReturn(configurationDto)

        val result = configurationService.save(configurationDto)

        val captor = argumentCaptor<Configuration>()
        verify(configurationRepository).saveWithVersion(captor.capture())

        assertEquals(configuration.integrationId, captor.firstValue.integrationId)
        assertEquals(configurationDto, result)
    }

    @Test
    fun testUpdateById() {
        val configurationPatchDto = ConfigurationPatchDto(null, null, "Test comment", null)
        val configuration = Configuration().apply { integrationId = 123L }

        whenever(configurationRepository.findById(any())).thenReturn(Optional.of(configuration))
        whenever(
            configurationMappingService.toDto(any(), any()),
        ).thenReturn(ConfigurationDto.builder().integrationId(123L).build())
        whenever(configurationRepository.saveWithVersion(any<Configuration>())).thenReturn(configuration)

        val result = configurationService.updateById(123L, configurationPatchDto)

        val captor = argumentCaptor<Configuration>()
        verify(configurationRepository).saveWithVersion(captor.capture())

        assertEquals(configuration.integrationId, captor.firstValue.integrationId)
        assertEquals("Test comment", captor.firstValue.comment)
        assertEquals(result.integrationId, captor.firstValue.integrationId)
    }

    @Test
    fun testUpdateByIdNotFound() {
        val configurationPatchDto = ConfigurationPatchDto(null, null, "Test comment", null)

        whenever(configurationRepository.findById(any())).thenReturn(Optional.empty())

        assertThrows(IllegalArgumentException::class.java) {
            configurationService.updateById(123L, configurationPatchDto)
        }
    }

    @Test
    fun testDeleteById() {
        configurationService.deleteById(123L)

        verify(configurationRepository).deleteById(123L)
    }
}
