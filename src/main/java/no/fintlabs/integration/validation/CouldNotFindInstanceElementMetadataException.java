package no.fintlabs.integration.validation;

public class CouldNotFindInstanceElementMetadataException extends RuntimeException {

    public CouldNotFindInstanceElementMetadataException(Long metadataId) {
        super("Could not find instance element metadata for metadataId=" + metadataId);
    }
}
