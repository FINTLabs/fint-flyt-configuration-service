package no.fintlabs.integration;

import no.fintlabs.integration.model.IntegrationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class IntegrationConfigurationService {

    private final IntegrationConfigurationRepository integrationConfigurationRepository;

    public IntegrationConfigurationService(IntegrationConfigurationRepository integrationConfigurationRepository) {
        this.integrationConfigurationRepository = integrationConfigurationRepository;
    }

    public IntegrationConfiguration newIntegrationConfiguration(IntegrationConfiguration integrationConfiguration) {
        integrationConfiguration.setVersion(1);
        integrationConfiguration.setId(UUID.randomUUID().toString());

        return integrationConfigurationRepository.save(integrationConfiguration);
    }

    public void addNewIntegrationConfigurationVersion(String id, IntegrationConfiguration integrationConfiguration) {
        List<IntegrationConfiguration> integrationConfigurations = integrationConfigurationRepository.getIntegrationConfigurationByIdOrderByVersionDesc(integrationConfiguration.getId());

        if (integrationConfiguration.isSameAs(id) && integrationConfigurations.size() > 0) {
            integrationConfiguration.setVersion(integrationConfigurations.get(0).getVersion() + 1);
            integrationConfiguration.setDocumentId(null);
            integrationConfigurationRepository.save(integrationConfiguration);
        } else {
            throw new IntegrationConfigurationNotFound();
        }
    }

    public void deleteIntegrationConfigurationById(String id) {
        integrationConfigurationRepository.deleteIntegrationConfigurationById(id);
    }

    public List<IntegrationConfiguration> getIntegrationConfigurationById(String id) {
        return integrationConfigurationRepository.getIntegrationConfigurationById(id);
    }

    public IntegrationConfiguration getLatestIntegrationConfigurationById(String id) {
        List<IntegrationConfiguration> latest = integrationConfigurationRepository.getIntegrationConfigurationByIdOrderByVersionDesc(id);

        if (latest.size() > 0) {
            return latest.get(0);
        }

        throw new IntegrationConfigurationNotFound();

    }

    public Page<IntegrationConfiguration> getAllIntegrationConfiguration(Pageable pageable) {
        return integrationConfigurationRepository.findAll(pageable);
    }

    public Optional<IntegrationConfiguration> getIntegrationConfigurationByIdAndVersion(String id, int version) {
        return Optional.ofNullable(
                integrationConfigurationRepository.getIntegrationConfigurationByIdAndVersion(id, version)
        );
    }
}
