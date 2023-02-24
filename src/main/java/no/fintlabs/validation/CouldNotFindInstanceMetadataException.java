package no.fintlabs.validation;

public class CouldNotFindInstanceMetadataException extends RuntimeException {

    public CouldNotFindInstanceMetadataException(Long metadataId) {
        super("Could not find instance metadata for metadataId=" + metadataId);
    }
}
