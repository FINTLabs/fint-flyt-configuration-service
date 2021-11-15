package no.fintlabs.integration;

import no.fintlabs.integration.model.IntegrationConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntegrationConfigurationRepository extends MongoRepository<IntegrationConfiguration, String> {
    List<IntegrationConfiguration> getIntegrationConfigurationById(String id);
    IntegrationConfiguration getIntegrationConfigurationByIdAndVersion(String id, int version);
    List<IntegrationConfiguration> getIntegrationConfigurationByIdOrderByVersionDesc(String id);
    void deleteIntegrationConfigurationById(String id);
}
