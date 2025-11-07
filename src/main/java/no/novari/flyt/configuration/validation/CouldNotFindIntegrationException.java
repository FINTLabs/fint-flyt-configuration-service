package no.novari.flyt.configuration.validation;

public class CouldNotFindIntegrationException extends RuntimeException {

    public CouldNotFindIntegrationException(Long integrationId) {
        super("Could not find integration with id=" + integrationId);
    }

}
