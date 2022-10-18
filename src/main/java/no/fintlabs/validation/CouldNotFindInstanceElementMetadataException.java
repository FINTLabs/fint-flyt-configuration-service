package no.fintlabs.validation;

public class CouldNotFindInstanceElementMetadataException extends RuntimeException {

    public CouldNotFindInstanceElementMetadataException(Long metadataId) {
        super("Could not find instance element metadata for metadataId=" + metadataId);
    }
}
