package no.fintlabs.integration;

import no.fintlabs.integration.model.IntegrationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntegrationConfigurationRepository extends JpaRepository<IntegrationConfiguration, String> {
    List<IntegrationConfiguration> getIntegrationConfigurationBySourceApplicationIntegrationId(String id);

    IntegrationConfiguration getIntegrationConfigurationBySourceApplicationIntegrationIdAndVersion(String id, int version);

    List<IntegrationConfiguration> getIntegrationConfigurationBySourceApplicationIntegrationIdOrderByVersionDesc(String id);

    Optional<IntegrationConfiguration> findFirstBySourceApplicationIntegrationIdOrderByVersionDesc(String id);

    void deleteIntegrationConfigurationBySourceApplicationIntegrationId(String id);
}
