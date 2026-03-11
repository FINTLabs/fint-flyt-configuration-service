package no.novari.flyt.configuration.mapping

import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationDto
import no.novari.flyt.configuration.model.configuration.dtos.ObjectMappingDto
import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto
import no.novari.flyt.configuration.model.configuration.entities.Configuration
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    classes = [
        ObjectsFromCollectionMappingMappingService::class,
        ObjectCollectionMappingMappingService::class,
        ValuesFromCollectionMappingMappingService::class,
        ValueCollectionMappingMappingService::class,
        InstanceCollectionReferencesMappingService::class,
        PerKeyMappingService::class,
        ValueMappingMappingService::class,
        ObjectMappingMappingService::class,
        ConfigurationMappingService::class,
    ],
)
class ConfigurationMappingIntegrationTest {
    @Autowired
    lateinit var configurationMappingService: ConfigurationMappingService

    private lateinit var configuration: Configuration
    private lateinit var configurationDto: ConfigurationDto

    @BeforeEach
    fun setup() {
        configurationDto =
            ConfigurationDto
                .builder()
                .integrationId(1L)
                .integrationMetadataId(2L)
                .mapping(
                    ObjectMappingDto
                        .builder()
                        .valueMappingPerKey(
                            mutableMapOf(
                                "field" to
                                    ValueMappingDto
                                        .builder()
                                        .type(ValueMapping.Type.STRING)
                                        .mappingString("abc")
                                        .build(),
                            ),
                        ).build(),
                ).build()

        configuration = configurationMappingService.toEntity(configurationDto)
    }

    @Test
    fun shouldKeepAllValuesWhenMappingToDtoWithMapping() {
        val result = configurationMappingService.toDto(configuration, false)
        assertEquals(configurationDto, result)
    }

    @Test
    fun shouldKeepAllValuesWhenMappingToDtoWithoutMapping() {
        val result = configurationMappingService.toDto(configuration, true)

        assertEquals(
            ConfigurationDto
                .builder()
                .integrationId(1L)
                .integrationMetadataId(2L)
                .build(),
            result,
        )
    }

    @Test
    fun shouldKeepAllValuesWhenMappingToConfigurationAndThenBackToDto() {
        val firstResult = configurationMappingService.toEntity(configurationDto)
        val secondResult = configurationMappingService.toDto(firstResult, false)
        assertEquals(configurationDto, secondResult)
    }
}
