package no.fintlabs.integration;


import no.fintlabs.integration.model.configuration.entities.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    // TODO: 18/10/2022 Lock
    default Configuration saveWithVersion(Configuration configuration) {
        if (configuration.isCompleted()) {
            int nextVersion = getNextVersionForIntegrationId(configuration.getIntegrationId());
            configuration.setVersion(nextVersion);
        }
        return save(configuration);
    }

    Collection<Configuration> findConfigurationsByIntegrationId(Long integrationId);

    Optional<Configuration> findFirstByIntegrationIdAndVersionNotNullOrderByVersionDesc(Long integrationId);

    default int getNextVersionForIntegrationId(Long integrationId) {
        return findFirstByIntegrationIdAndVersionNotNullOrderByVersionDesc(integrationId)
                .map(Configuration::getVersion)
                .map(version -> version + 1)
                .orElse(1);
    }

}
