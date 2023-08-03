package no.fintlabs;

import no.fintlabs.mapping.ConfigurationMappingService;
import no.fintlabs.model.configuration.dtos.ConfigurationDto;
import no.fintlabs.model.configuration.dtos.ConfigurationPatchDto;
import no.fintlabs.model.configuration.entities.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConfigurationServiceTest {
    @Mock
    private ConfigurationRepository configurationRepository;

    @Mock
    private ConfigurationMappingService configurationMappingService;

    @Mock
    private ObjectMappingRepository objectMappingRepository;

    private ConfigurationService configurationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        configurationService = new ConfigurationService(configurationRepository, configurationMappingService, objectMappingRepository);
    }

    @Test
    public void testFindById() {
        Configuration configuration = new Configuration();
        configuration.setIntegrationId(123L);

        ConfigurationDto configurationDto = ConfigurationDto.builder().integrationId(123L).build();

        when(configurationRepository.findById(anyLong())).thenReturn(Optional.of(configuration));
        when(configurationMappingService.toDto(any(), anyBoolean())).thenReturn(configurationDto);

        Optional<ConfigurationDto> result = configurationService.findById(123L, true);

        assertTrue(result.isPresent());
        assertEquals(configurationDto, result.get());
    }

    @Test
    public void testFindAll() {
        Configuration configuration = new Configuration();
        configuration.setIntegrationId(123L);

        ConfigurationDto configurationDto = ConfigurationDto.builder().integrationId(123L).build();

        when(configurationRepository.findAll(ArgumentMatchers.any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(configuration)));

        when(configurationMappingService.toDto(any(), anyBoolean())).thenReturn(configurationDto);

        ConfigurationFilter filter = ConfigurationFilter.builder().integrationId(123L).build();

        Page<ConfigurationDto> result = configurationService.findAll(filter, true, Pageable.unpaged());

        assertFalse(result.isEmpty());
        assertEquals(configurationDto, result.getContent().get(0));
    }

    @Test
    public void testSave() {
        ConfigurationDto configurationDto = ConfigurationDto.builder().integrationId(123L).build();
        Configuration configuration = Configuration
                .builder()
                .integrationId(123L)
                .build();

        when(configurationMappingService.toEntity((ConfigurationDto) any())).thenReturn(configuration);
        when(configurationRepository.saveWithVersion(any())).thenReturn(configuration);
        when(configurationMappingService.toDto(any(), anyBoolean())).thenReturn(configurationDto);

        ConfigurationDto result = configurationService.save(configurationDto);

        ArgumentCaptor<Configuration> captor = ArgumentCaptor.forClass(Configuration.class);
        verify(configurationRepository).saveWithVersion(captor.capture());

        assertEquals(configuration.getIntegrationId(), captor.getValue().getIntegrationId());
        assertEquals(configurationDto, result);
    }

    @Test
    public void testUpdateById() {
        ConfigurationPatchDto configurationPatchDto = ConfigurationPatchDto.builder().comment("Test comment").build();
        Configuration configuration = new Configuration();
        configuration.setIntegrationId(123L);

        when(configurationRepository.findById(anyLong())).thenReturn(Optional.of(configuration));
        when(configurationMappingService.toDto(any(), anyBoolean())).thenReturn(ConfigurationDto.builder().integrationId(123L).build());

        ConfigurationDto result = configurationService.updateById(123L, configurationPatchDto);

        ArgumentCaptor<Configuration> captor = ArgumentCaptor.forClass(Configuration.class);
        verify(configurationRepository).saveWithVersion(captor.capture());

        assertEquals(configuration.getIntegrationId(), captor.getValue().getIntegrationId());
        assertEquals("Test comment", captor.getValue().getComment());
        assertEquals(result.getIntegrationId(), captor.getValue().getIntegrationId());
    }

    @Test
    public void testUpdateById_NotFound() {
        ConfigurationPatchDto configurationPatchDto = ConfigurationPatchDto.builder().comment("Test comment").build();

        when(configurationRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> configurationService.updateById(123L, configurationPatchDto));
    }

    @Test
    public void testDeleteById() {
        doNothing().when(configurationRepository).deleteById(anyLong());

        configurationService.deleteById(123L);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(configurationRepository).deleteById(captor.capture());

        assertEquals(123L, captor.getValue());
    }

}

