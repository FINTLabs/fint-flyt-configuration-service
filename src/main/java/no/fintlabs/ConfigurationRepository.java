package no.fintlabs;


import no.fintlabs.model.configuration.entities.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    @Retryable(
            maxAttempts = 4,
            value = ConstraintViolationException.class,
            backoff = @Backoff(
                    delay = 1000,
                    multiplier = 2
            )
    )
    @Transactional
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
