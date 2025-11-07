package no.novari.configuration;

import no.novari.configuration.model.configuration.entities.Configuration;
import no.novari.configuration.ConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ConfigurationRepositoryTest {

    private ConfigurationRepository configurationRepository;
    private Configuration configuration;

    @BeforeEach
    public void setUp() {
        configurationRepository = Mockito.mock(ConfigurationRepository.class);
        configuration = Configuration
                .builder()
                .integrationId(1L)
                .integrationMetadataId(1L)
                .version(1)
                .completed(true)
                .build();
    }

    @Test
    public void testSaveWithVersion() {
        when(configurationRepository.saveWithVersion(configuration)).thenCallRealMethod();
        when(configurationRepository.getNextVersionForIntegrationId(configuration.getIntegrationId())).thenReturn(2);
        when(configurationRepository.save(configuration)).thenReturn(configuration);

        Configuration savedConfiguration = configurationRepository.saveWithVersion(configuration);

        assertEquals(2, savedConfiguration.getVersion());
    }

    @Test
    public void testGetNextVersionForIntegrationId() {
        when(configurationRepository.getNextVersionForIntegrationId(configuration.getIntegrationId())).thenCallRealMethod();
        when(configurationRepository.findFirstByIntegrationIdAndVersionNotNullOrderByVersionDesc(configuration.getIntegrationId())).thenReturn(Optional.of(configuration));

        int nextVersion = configurationRepository.getNextVersionForIntegrationId(configuration.getIntegrationId());

        assertEquals(2, nextVersion);
    }
}
