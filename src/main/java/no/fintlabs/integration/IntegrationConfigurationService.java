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

    public void addNewIntegrationConfigurationVersion(IntegrationConfiguration integrationConfiguration) {
        integrationConfiguration.setVersion(integrationConfiguration.getVersion() + 1);
        integrationConfiguration.setDocumentId(null);

        List<IntegrationConfiguration> integrationConfigurations = integrationConfigurationRepository.getIntegrationConfigurationById(integrationConfiguration.getId());

        if (integrationConfigurations.size() > 0) {
            integrationConfigurationRepository.save(integrationConfiguration);
            return;
        }

        throw new IntegrationConfigurationNotFound();
    }

    public List<IntegrationConfiguration> getIntegrationConfigurationById(String id) {
        return integrationConfigurationRepository.getIntegrationConfigurationById(id);
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
