package no.fintlabs.integration;


import no.fintlabs.integration.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    Collection<Configuration> findConfigurationsByIntegrationIdLike(Long integrationId);

    Optional<Configuration> findFirstByIntegrationIdLikeAndVersionNotNullOrderByVersionDesc(Long integrationId);

    default int getNextVersionForIntegrationId(Long integrationId) {
        return findFirstByIntegrationIdLikeAndVersionNotNullOrderByVersionDesc(integrationId)
                .map(Configuration::getVersion)
                .map(version -> version + 1)
                .orElse(1);
    }

}
