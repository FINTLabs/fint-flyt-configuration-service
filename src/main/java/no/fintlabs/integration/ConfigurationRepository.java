package no.fintlabs.integration;


import no.fintlabs.integration.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConfigurationRepository extends JpaRepository<Configuration, UUID> {

    Optional<Configuration> findFirstByIntegrationIdLikeAndVersionNotNullOrderByVersionDesc(String integrationId);

    default int getNextVersionForIntegrationId(String integrationId) {
        return findFirstByIntegrationIdLikeAndVersionNotNullOrderByVersionDesc(integrationId)
                .map(Configuration::getVersion)
                .map(version -> version + 1)
                .orElse(1);
    }

}
