package no.novari.flyt.configuration.validation

class CouldNotFindIntegrationException(
    integrationId: Long,
) : RuntimeException("Could not find integration with id=$integrationId")
