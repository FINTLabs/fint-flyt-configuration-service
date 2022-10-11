package no.fintlabs.integration;

import no.fintlabs.integration.model.Configuration;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public Collection<Configuration> findAll() {
        return configurationRepository.findAll();
    }

    public Collection<Configuration> findAllForIntegrationId(Long integrationId) {
        return configurationRepository.findConfigurationsByIntegrationId(integrationId);
    }

    public Optional<Configuration> findById(Long configurationId) {
        return configurationRepository.findById(configurationId);
    }

    @Transactional
    public Configuration save(Configuration configuration) {
        if (configuration.isCompleted()) {
            int nextVersion = configurationRepository.getNextVersionForIntegrationId(configuration.getIntegrationId());
            configuration.setVersion(nextVersion);
        }
        return configurationRepository.save(configuration);
    }

}
