package no.fintlabs.integration;

import no.fintlabs.integration.model.Configuration;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public Collection<Configuration> findAll() {
        return configurationRepository.findAll();
    }

    public Optional<Configuration> findById(UUID configurationId) {
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

//    @Transactional
//    public Optional<Configuration> update(Long configurationId, Configuration configuration) {
//        Configuration existingConfiguration = configurationRepository.findById(configurationId);
//
//
//
//        if (configuration.isCompleted()) {
//            int nextVersion = configurationRepository.getNextVersionForIntegrationId(configuration.getIntegrationId());
//            configuration.setVersion(nextVersion);
//        }
//        return configurationRepository.save(configuration);
//    }

}
