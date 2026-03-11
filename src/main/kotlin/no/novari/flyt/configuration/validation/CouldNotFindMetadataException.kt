package no.novari.flyt.configuration.validation

class CouldNotFindMetadataException(
    metadataId: Long,
) : RuntimeException("Could not find metadata with id=$metadataId")
