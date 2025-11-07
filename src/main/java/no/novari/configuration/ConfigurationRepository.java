package no.novari.configuration;

import no.novari.configuration.model.configuration.entities.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    default Configuration saveWithVersion(Configuration configuration) {
        if (configuration.isCompleted()) {
            int nextVersion = getNextVersionForIntegrationId(configuration.getIntegrationId());
            configuration.setVersion(nextVersion);
        }
        return save(configuration);
    }

    default int getNextVersionForIntegrationId(Long integrationId) {
        return findFirstByIntegrationIdAndVersionNotNullOrderByVersionDesc(integrationId)
                .map(Configuration::getVersion)
                .map(version -> version + 1)
                .orElse(1);
    }

    Optional<Configuration> findFirstByIntegrationIdAndVersionNotNullOrderByVersionDesc(Long integrationId);

}
