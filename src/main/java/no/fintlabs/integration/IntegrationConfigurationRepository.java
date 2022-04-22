package no.fintlabs.integration;

import no.fintlabs.integration.model.IntegrationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntegrationConfigurationRepository extends JpaRepository<IntegrationConfiguration, String> {
    List<IntegrationConfiguration> getIntegrationConfigurationByIntegrationId(String id);

    IntegrationConfiguration getIntegrationConfigurationByIntegrationIdAndVersion(String id, int version);

    List<IntegrationConfiguration> getIntegrationConfigurationByIntegrationIdOrderByVersionDesc(String id);

    Optional<IntegrationConfiguration> findFirstByIntegrationIdOrderByVersionDesc(String id);

    void deleteIntegrationConfigurationByIntegrationId(String id);
}
