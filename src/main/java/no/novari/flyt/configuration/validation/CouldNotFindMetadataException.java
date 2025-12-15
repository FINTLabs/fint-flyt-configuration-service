package no.novari.flyt.configuration.validation;

public class CouldNotFindMetadataException extends RuntimeException {

    public CouldNotFindMetadataException(Long metadataId) {
        super("Could not find metadata with id=" + metadataId);
    }
}
