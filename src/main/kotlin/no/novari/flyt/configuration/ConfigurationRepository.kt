package no.novari.flyt.configuration

import no.novari.flyt.configuration.model.configuration.entities.Configuration
import org.springframework.data.jpa.repository.JpaRepository

interface ConfigurationRepository : JpaRepository<Configuration, Long> {
    fun saveWithVersion(configuration: Configuration): Configuration {
        if (configuration.isCompleted) {
            val nextVersion = getNextVersionForIntegrationId(requireNotNull(configuration.integrationId))
            configuration.version = nextVersion
        }
        return save(configuration)
    }

    fun getNextVersionForIntegrationId(integrationId: Long): Int =
        findFirstByIntegrationIdAndVersionNotNullOrderByVersionDesc(integrationId)
            ?.version
            ?.plus(1)
            ?: 1

    fun findFirstByIntegrationIdAndVersionNotNullOrderByVersionDesc(integrationId: Long): Configuration?
}
