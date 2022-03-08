package no.fintlabs.integration;

import no.fintlabs.integration.model.IntegrationConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IntegrationConfigurationService {

    private final IntegrationConfigurationRepository integrationConfigurationRepository;

    public IntegrationConfigurationService(IntegrationConfigurationRepository integrationConfigurationRepository) {
        this.integrationConfigurationRepository = integrationConfigurationRepository;
    }

    public IntegrationConfiguration newIntegrationConfiguration(IntegrationConfiguration integrationConfiguration) {
        integrationConfiguration.setVersion(1);
        integrationConfiguration.setIntegrationId(UUID.randomUUID().toString());

        return integrationConfigurationRepository.save(integrationConfiguration);
    }

    public void addNewIntegrationConfigurationVersion(String id, IntegrationConfiguration integrationConfiguration) {
        List<IntegrationConfiguration> integrationConfigurations
                = integrationConfigurationRepository.getIntegrationConfigurationByIntegrationIdOrderByVersionDesc(integrationConfiguration.getIntegrationId());

        if (integrationConfiguration.isSameAs(id) && integrationConfigurations.size() > 0) {
            integrationConfiguration.setVersion(integrationConfigurations.get(0).getVersion() + 1);
            integrationConfiguration.setDocumentId(null);
            integrationConfigurationRepository.save(integrationConfiguration);
        } else {
            throw new IntegrationConfigurationNotFound();
        }
    }

    public void deleteIntegrationConfigurationById(String id) {
        integrationConfigurationRepository.deleteIntegrationConfigurationByIntegrationId(id);
    }

    public List<IntegrationConfiguration> getIntegrationConfigurationById(String id) {
        return integrationConfigurationRepository.getIntegrationConfigurationByIntegrationIdOrderByVersionDesc(id);
    }

    public List<IntegrationConfiguration> getLatestIntegrationConfigurations() {

        return integrationConfigurationRepository
                .findAll()
                .stream()
                .filter(ic -> getLatestIntegrationConfigurationById(ic.getIntegrationId()).getVersion() == ic.getVersion())
                .collect(Collectors.toList());
    }

    public IntegrationConfiguration getLatestIntegrationConfigurationById(String id) {

        List<IntegrationConfiguration> latest
                = integrationConfigurationRepository.getIntegrationConfigurationByIntegrationIdOrderByVersionDesc(id);

        if (latest.size() > 0) {
            return latest.get(0);
        }

        throw new IntegrationConfigurationNotFound();
    }

    public List<IntegrationConfiguration> getAllIntegrationConfiguration() {
        return integrationConfigurationRepository.findAll();
    }

    public Optional<IntegrationConfiguration> getIntegrationConfigurationByIdAndVersion(String id, int version) {
        return Optional.ofNullable(
                integrationConfigurationRepository.getIntegrationConfigurationByIntegrationIdAndVersion(id, version)
        );
    }

}
